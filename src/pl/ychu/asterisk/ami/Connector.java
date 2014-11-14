package pl.ychu.asterisk.ami;

import pl.ychu.asterisk.AsteriskConfiguration;
import pl.ychu.asterisk.ami.action.*;

import java.io.IOException;
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
    private AsteriskConfiguration configuration;
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

    protected Connector() {
        this.mutex = new Object();
        this.handlers = new ArrayList<EventHandler>();
        this.actionIdFactory = new ActionId();
        this.client = new Socket();
        this.toSend = new HashMap<String, ResponseHandler>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^(Response:).*");
    }

    public Connector(AsteriskConfiguration configuration, boolean listenEvents) throws IOException {
        this();
        this.configuration = configuration;
        this.listenEvents = listenEvents;
        this.createThread();
    }

    public Connector(AsteriskConfiguration configuration, EventHandler handler, boolean listenEvents) throws IOException {
        this(configuration, listenEvents);
        this.addEventHandler(handler);
        this.start();
    }

    private void createThread() {
        this.mainThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        client.connect(new InetSocketAddress(configuration.getHostName(), configuration.getHostPort()));
                        reader = new Reader(client.getInputStream());
                        writer = new Writer(client.getOutputStream());
                        Login l = new Login(configuration.getUserName(), configuration.getUserPassword(), listenEvents);
                        writer.send(l);
                        String message = reader.readMessage();
                        if (message.contains("Success")) {
                            while (!Thread.currentThread().isInterrupted()) {
                                message = reader.readMessage();
                                if (eventPattern.matcher(message).find()) {
                                    Event e = Event.parseEvent(message);
                                    synchronized (mutex) {
                                        for (EventHandler handler : handlers) {
                                            new Thread(new EventAsyncHelper(e, handler)).start();
                                        }
                                    }
                                    continue;
                                }
                                if (responsePattern.matcher(message).find()) {
                                    Response r = new Response(message);
                                    ResponseHandler handler = toSend.remove(r.getActionId());
                                    if (handler != null) {
                                        new Thread(new ResponseAsyncHelper(r, handler)).start();
                                    }
                                    continue;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                } catch (IOException ex) {
                }
            }
        };
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

    public static void main(String[] args) throws IOException, InterruptedException {
        AsteriskConfiguration conf = new AsteriskConfiguration("192.168.24.4", 5038, "admin", "holi!holi9");
        Connector conn = new Connector(conf, new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                System.out.println(event.getMessage());
            }
        }, true);
        Thread.sleep(1000);
        conn.sendAction(new QueueStatus(null, null), new ResponseHandler() {
            @Override
            public void onResponse(Response response) {
                System.out.print(response.getMessage());
            }
        });
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
            handler.onResponse(response);
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
