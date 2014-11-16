package pl.ychu.asterisk.ami;

import pl.ychu.asterisk.ami.action.Ping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Krzysztof on 2014-11-16.
 */
public class AsynchronizedConnection {
    private final Connection connection;
    private final ArrayList<EventHandler> handlers;
    private final Object mutex;
    private final ActionId actionIdFactory;
    private final HashMap<String, ResponseHandler> waitingForResponse;
    private final Pattern eventPattern;
    private final Pattern responsePattern;
    private Thread mainThread;
    private Thread maintainingThread;
    private Writer writer;
    private Reader reader;
    private boolean working;
    private boolean enabledMaintainingThread = true;

    public AsynchronizedConnection(Connection connection) {
        this.connection = connection;
        this.mutex = new Object();
        this.handlers = new ArrayList<EventHandler>();
        this.actionIdFactory = new ActionId();
        this.waitingForResponse = new HashMap<String, ResponseHandler>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^(Response:).*");
        this.createThread();
        this.createMaintainingThread();
    }

    public AsynchronizedConnection(Connection connection, EventHandler eventHandler) {
        this(connection);
        this.handlers.add(eventHandler);
    }

    public void enableMaintainingThread(boolean enabled) {
        this.enabledMaintainingThread = enabled;
    }

    public void sendAction(Action action) throws IOException {
        writer.send(action);
    }

    public void sendAction(Action action, ResponseHandler handler) throws IOException {
        action.setActionId(this.actionIdFactory.getNext());
        waitingForResponse.put(action.getActionId(), handler);
        writer.send(action);
    }

    public void start() throws IOException, NotAuthorizedException {
        reconnect();
        mainThread.start();
        if (this.enabledMaintainingThread) {
            maintainingThread.start();
        }
    }

    public void stop() {
        mainThread.interrupt();
        if (this.enabledMaintainingThread && maintainingThread.isAlive()) {
            maintainingThread.interrupt();
        }
    }

    private void reconnect() throws IOException, NotAuthorizedException {
        connection.connect();
        working = true;
        writer = connection.getWriter();
        reader = connection.getReader();
    }

    private void createThread() {
        this.mainThread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (!working) {
                            reconnect();
                        }
                        while (!Thread.currentThread().isInterrupted()) {
                            processMessage();
                        }
                    } catch (IOException ex) {
                        working = false;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex2) {
                        }
                    } catch (NotAuthorizedException e) {
                        working = false;
                        break;
                    }
                }
            }
        };
    }

    private void createMaintainingThread() {
        maintainingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(connection.getReadTimeout() / 2);
                    } catch (InterruptedException ex) {
                        break;
                    }
                    try {
                        if (writer != null && connection.isConnected()) {
                            writer.send(new Ping());
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void processMessage() throws IOException {
        String message = reader.readMessage();
        if (eventPattern.matcher(message).find()) {
            processEvent(message);
            return;
        }
        if (responsePattern.matcher(message).find()) {
            processResponse(message);
            return;
        }
    }

    private void processEvent(String message) {
        Event e = Event.parseEvent(message);
        synchronized (mutex) {
            for (EventHandler handler : handlers) {
                new Thread(new EventAsyncHelper(e, handler)).start();
            }
        }
    }

    private void processResponse(String message) {
        Response r = new Response(message);
        ResponseHandler handler = waitingForResponse.remove(r.getActionId());
        if (handler != null) {
            new Thread(new ResponseAsyncHelper(r, handler)).start();
        } else {
            synchronized (mutex) {
                for (EventHandler evHandler : handlers) {
                    new Thread(new DefaultResponseAsyncHelper(r, evHandler)).start();
                }
            }
        }
    }

    private class ResponseAsyncHelper implements Runnable {

        private Response response;
        private ResponseHandler handler;

        public ResponseAsyncHelper(Response response, ResponseHandler handler) {
            this.response = response;
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.handleResponse(response);
        }
    }

    private class DefaultResponseAsyncHelper implements Runnable {
        private Response response;
        private EventHandler handler;

        public DefaultResponseAsyncHelper(Response response, EventHandler handler) {
            this.response = response;
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.handleResponse(response);
        }
    }

    private class EventAsyncHelper implements Runnable {
        private Event event;
        private EventHandler handler;

        public EventAsyncHelper(Event event, EventHandler handler) {
            this.event = event;
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.handleEvent(event);
        }
    }
}
