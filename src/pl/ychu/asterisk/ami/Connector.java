package pl.ychu.asterisk.ami;

import pl.ychu.asterisk.AsteriskConfiguration;
import pl.ychu.asterisk.ami.action.AbsoluteTimeout;
import pl.ychu.asterisk.ami.action.AgentLogoff;
import pl.ychu.asterisk.ami.action.ListCommands;
import pl.ychu.asterisk.ami.action.Login;

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
    private boolean running;
    private Thread mainThread;
    private Reader reader;
    private Writer writer;
    private ActionId actionIdFactory;
    private HashMap<String, ResponseHandler> toSend;
    private Pattern eventPattern;
    private Pattern responsePattern;

    protected Connector() {

    }

    public Connector(AsteriskConfiguration configuration) throws IOException {
        this.configuration = configuration;
        this.handlers = new ArrayList<EventHandler>();
        this.client = new Socket();
        this.actionIdFactory = new ActionId();
        this.toSend = new HashMap<String, ResponseHandler>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^(Response:).*");
        this.createThread();
    }

    public Connector(AsteriskConfiguration configuration, EventHandler handler) throws IOException {
        this(configuration);
        this.addEventHandler(handler);
        this.start();
    }

    private void createThread() {
        this.mainThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (running) {
                        client.connect(new InetSocketAddress(configuration.getHostName(), configuration.getHostPort()));
                        reader = new Reader(client.getInputStream());
                        writer = new Writer(client.getOutputStream());
                        Login l = new Login(configuration.getUserName(), configuration.getUserPassword(), true);
                        writer.send(l);
                        String message = reader.readMessage();
                        if (message.contains("Success")) {
                            while (running) {
                                message = reader.readMessage();
                                if (eventPattern.matcher(message).find()) {
                                    Event e = Event.parseEvent(message);
                                    for (EventHandler handler : handlers) {
                                        new Thread(new EventAsyncHelper(e, handler)).start();
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
                            running = false;
                            break;
                        }
                    }
                } catch (IOException ex) {
                    running = false;
                }
            }
        };
    }

    public void addEventHandler(EventHandler handler) {
        handlers.add(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        handlers.remove(handler);
    }

    public void start() {
        this.running = true;
        this.mainThread.start();
    }

    public void stop() {
        this.running = false;
        this.mainThread.interrupt();
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
        });
        Thread.sleep(1000);
        conn.sendAction(new ListCommands(), new ResponseHandler() {
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
