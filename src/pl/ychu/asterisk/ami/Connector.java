package pl.ychu.asterisk.ami;

import pl.ychu.asterisk.ami.action.Command;
import pl.ychu.asterisk.ami.action.Login;
import pl.ychu.asterisk.ami.action.Ping;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class Connector {
    private ArrayList<EventHandler> handlers;
    private Configuration configuration;
    private Socket client;
    private Thread mainThread;
    private Thread maintainingThread;
    private Reader reader;
    private Writer writer;
    private ActionId actionIdFactory;
    private HashMap<String, ResponseHandler> toSend;
    private Pattern eventPattern;
    private Pattern responsePattern;
    private Pattern loginPattern;
    private boolean listenEvents;
    private final Object mutex;
    private boolean connected;
    private int readTimeout = 30000;
    private int connectTimeout = 5000;

    protected Connector() {
        this.mutex = new Object();
        this.handlers = new ArrayList<EventHandler>();
        this.actionIdFactory = new ActionId();
        this.toSend = new HashMap<String, ResponseHandler>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^(Response:).*");
        this.loginPattern = Pattern.compile("^.*(Success).*");
        this.connected = false;
    }

    public Connector(Configuration configuration, boolean listenEvents) {
        this();
        this.configuration = configuration;
        this.listenEvents = listenEvents;
        this.createMaintainingThread();
        this.createThread();
    }

    public Connector(Configuration configuration, EventHandler handler, boolean listenEvents) {
        this(configuration, listenEvents);
        this.addEventHandler(handler);
    }

    private void createMaintainingThread() {
        maintainingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(readTimeout);
                    } catch (InterruptedException ex) {
                        break;
                    }
                    try {
                        if (writer != null && connected) {
                            writer.send(new Ping());
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void createThread() {
        this.mainThread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (!connected) {
                            connect();
                        }
                        while (!Thread.currentThread().isInterrupted()) {
                            processMessage();
                        }
                    } catch (IOException ex) {
                        connected = false;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    } catch (NotAuthorizedException e) {
                        break;
                    }
                }
            }
        };
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

    private void connect() throws IOException, NotAuthorizedException {
        client = new Socket();
        client.setSoTimeout(readTimeout * 2);
        client.connect(new InetSocketAddress(configuration.getHostName(), configuration.getHostPort()), connectTimeout);
        reader = new Reader(client.getInputStream());
        writer = new Writer(client.getOutputStream());
        connected = true;
        Login l = new Login(configuration.getUserName(), configuration.getUserPassword(), listenEvents);
        writer.send(l);
        if (loginPattern.matcher(reader.readMessage()).find()) {
            throw new NotAuthorizedException("Bad user name or secret.");
        }
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout * 2;
    }

    public void setReadTimeout(int readTimeout) throws IOException {
        if (client != null) {
            client.setSoTimeout(readTimeout);
        }
        this.readTimeout = readTimeout / 2;
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

    public void start() throws IOException, NotAuthorizedException {
        this.connect();
        mainThread.start();
        if (this.configuration.isEnabledMaintainingThread()) {
            maintainingThread.start();
        }
    }

    public void stop() {
        mainThread.interrupt();
        if (this.configuration.isEnabledMaintainingThread() && maintainingThread.isAlive()) {
            maintainingThread.interrupt();
        }
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

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException {
        final Configuration conf = new Configuration("192.168.24.4", 5038, "admin", "holi!holi9");
        conf.enableRunMaintainingThread(true);
        final Connector conn = new Connector(conf, new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                //System.out.println(System.currentTimeMillis() + ": " + event.getEventName());
            }

            @Override
            public void handleResponse(Response response) {
                //System.out.println(response.getMessage());
            }
        }, true);
        conn.start();
        SynchronizedActionSender actionSender = new SynchronizedActionSender(conn);
        long star = System.currentTimeMillis();
        Response response = actionSender.send(new Command("sip show peers"));
        System.out.println("Execution time: " + (System.currentTimeMillis() - star) + " ms");
        System.out.println("Message length: " + response.getMessage().length() + " b");
        conn.stop();
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
