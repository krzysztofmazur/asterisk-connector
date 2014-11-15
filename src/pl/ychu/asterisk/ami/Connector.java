package pl.ychu.asterisk.ami;

import pl.ychu.asterisk.ami.action.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class Connector {
    private ArrayList<EventHandler> handlers;
    private Configuration configuration;
    private Socket client;
    private Thread mainThread;
    private Reader reader;
    private Writer writer;
    private ActionId actionIdFactory;
    private HashMap<String, ResponseHandler> toSend;
    private Pattern eventPattern;
    private Pattern responsePattern;
    private boolean listenEvents;
    private final Object mutex;
    private boolean connected;

    protected Connector() {
        this.mutex = new Object();
        this.handlers = new ArrayList<EventHandler>();
        this.actionIdFactory = new ActionId();
        this.toSend = new HashMap<String, ResponseHandler>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^(Response:).*");
        this.connected = false;
    }

    public Connector(Configuration configuration, boolean listenEvents) throws IOException {
        this();
        this.configuration = configuration;
        this.listenEvents = listenEvents;
        this.createThread();
    }

    public Connector(Configuration configuration, EventHandler handler, boolean listenEvents) throws IOException {
        this(configuration, listenEvents);
        this.addEventHandler(handler);
        this.start();
    }

    private void createThread() {
        this.mainThread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        client = new Socket();
                        client.setSoTimeout(5000);
                        client.connect(new InetSocketAddress(configuration.getHostName(), configuration.getHostPort()), 5000);
                        reader = new Reader(client.getInputStream());
                        writer = new Writer(client.getOutputStream());
                        connected = true;
                        Login l = new Login(configuration.getUserName(), configuration.getUserPassword(), listenEvents);
                        writer.send(l);
                        String message = reader.readMessage();
                        if (message.contains("Success")) {
                            while (!Thread.currentThread().isInterrupted()) {
                                message = reader.readMessage();
                                if (eventPattern.matcher(message).find()) {
                                    processEvent(message);
                                    continue;
                                }
                                if (responsePattern.matcher(message).find()) {
                                    processResponse(message);
                                    continue;
                                }
                            }
                        } else {
                            reader.close();
                            writer.close();
                            client.close();
                            break;
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        connected = false;
                    }
                }
            }
        };
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
        ResponseHandler handler = toSend.remove(r.getActionId());
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

    public void addEventHandler(EventHandler handler) {
        synchronized (mutex) {
            handlers.add(handler);
        }
    }

    public void removeEventHandler(EventHandler handler) {
        synchronized (mutex) {
            handlers.remove(handler);
        }
    }

    public void start() {
        mainThread.start();
    }

    public void stop() {
        mainThread.interrupt();
    }

    public void sendAction(Action action) throws IOException {
        writer.send(action);
    }

    public void sendAction(Action action, ResponseHandler handler) throws IOException {
        action.setActionId(this.actionIdFactory.getNext());
        toSend.put(action.getActionId(), handler);
        writer.send(action);
    }

    public boolean isConnected() {
        return connected;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Configuration conf = new Configuration("192.168.24.4", 5038, "admin", "holi!holi9");
        final Connector conn = new Connector(conf, new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                //System.out.println(System.currentTimeMillis() + ": " + event.getEventName());
            }

            @Override
            public void handleResponse(Response response) {
                System.out.println(response.getMessage());
            }
        }, true);
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
