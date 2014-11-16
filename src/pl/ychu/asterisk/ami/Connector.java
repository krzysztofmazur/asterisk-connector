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
 * Klasa służąca do podłączenia się do interfejsu AMI.
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

    /**
     * Podstawowy konstruktor.
     *
     * @param configuration Konfiguracja połączenia do asteriska.
     * @param listenEvents  Argument decydujący czy będą nasłuchiwane eventy. Jeśli nie to odczytywane będą tylko
     *                      odpowiedzi na wysłane akcje.
     */
    public Connector(Configuration configuration, boolean listenEvents) {
        this();
        this.configuration = configuration;
        this.listenEvents = listenEvents;
        this.createMaintainingThread();
        this.createThread();
    }

    /**
     * Podstawowy konstruktor, do którego w parametrze przekazujemy obiekt służący do nasłuchiwania eventów.
     *
     * @param configuration Konfiguracja połączenia do asteriska.
     * @param handler       Obiekt implementujący interface EventHandler.
     * @param listenEvents  Argument decydujący czy będą nasłuchiwane eventy. Jeśli nie to odczytywane będą tylko
     *                      odpowiedzi na wysłane akcje.
     */
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
                        }
                    } catch (NotAuthorizedException e) {
                        connected = false;
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

    /**
     * @return Timeout dla połączenia do asteriska w milisekundach.
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Metoda ustawia timeout dla połączenia do asteriska. Jeśli nie jest wywoływana to wartość domyślna wynosi 5000
     * ms.
     *
     * @param connectTimeout Timeout dla połączenia do asteriska w milisekundach.
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * @return Timeout dla odczytania kolejnego znaku z AMI.
     */
    public int getReadTimeout() {
        return readTimeout * 2;
    }

    /**
     * Metoda ustawia timeout dla odczytania kolejnego znaku z AMI. Domyślna wartość to 30 s.
     *
     * @param readTimeout Timeout dla odczytania kolejnego znaku z AMI w milisekundach.
     * @throws IOException Wyjątek może wystąpić jeżeli przed wykonaniem tej metody została wykonana metoda start(), ale
     *                     połączenie nie zostało zestawione.
     */
    public void setReadTimeout(int readTimeout) throws IOException {
        if (client != null) {
            client.setSoTimeout(readTimeout);
        }
        this.readTimeout = readTimeout / 2;
    }

    /**
     * Metoda dodaje obiekt nasłuchujący eventy z asteriska. Wszystkie przekazane przez tą metodę obiekty będą używane
     * do przesyłania eventów.
     *
     * @param handler Obiekt implementujący interface EventHandler.
     */
    public void addEventHandler(EventHandler handler) {
        synchronized (mutex) {
            handlers.add(handler);
        }
    }

    /**
     * Metoda usuwa obiekt nasłuchujący eventy z asteriska.
     *
     * @param handler Referencja do przekazanego wcześniej obiektu implementującego interfejs EventHandler.
     */
    public void removeEventHandler(EventHandler handler) {
        synchronized (mutex) {
            handlers.remove(handler);
        }
    }

    /**
     * Metoda służy do połączenia obiektu do asteriska. Jeśli połączenie zostanie nawiązane i dane logowania
     * będą poprawne to zostaje uruchomiony wątek nasłuchujący eventy i odpowiedzi na przesłane żądania.
     *
     * @throws IOException            Wywoływany gdy na przekazanym hości i porcie nie udało się ustanowić połączenia.
     * @throws NotAuthorizedException Wywoływany gdy hasło lub login przekazane w konfiguracji są nieprawidłowe.
     */
    public void start() throws IOException, NotAuthorizedException {
        this.connect();
        mainThread.start();
        if (this.configuration.isEnabledMaintainingThread()) {
            maintainingThread.start();
        }
    }

    /**
     * Medtoda sługży do zatrzymania wątków nasłuchujących eventów i podtrzymujących połączenie.
     */
    public void stop() {
        mainThread.interrupt();
        if (this.configuration.isEnabledMaintainingThread() && maintainingThread.isAlive()) {
            maintainingThread.interrupt();
        }
    }

    /**
     * Metoda wysyła akcję do asteriska. Odpowiedź zostanie zwrócona do wszystkich przekazanych obiektów nasłuchujących
     * eventów z asteriska do metody handleResponse().
     *
     * @param action Obiekt roszerzający klasę Action.
     * @throws IOException Wywoływany jeśli metoda jest wywołana gdy nie zostało ustanowione połączenie do asteriska.
     */
    public void sendAction(Action action) throws IOException {
        writer.send(action);
    }

    /**
     * Metoda wysyła akcję do asteriska. Odpowiedź zostanie zwrócona tylko do objektu implementującego ResponseHandler,
     * który został przekazany w parametrze.
     *
     * @param action  Obiekt roszerzający klasę Action.
     * @param handler Obiekt implementujący ResponseHandler, w którym zostanie wywołana metoda handleResponse() w
     *                momencie otrzymania odpowiedzi.
     * @throws IOException Wywoływany jeśli metoda jest wywołana gdy nie zostało ustanowione połączenie do asteriska.
     */
    public void sendAction(Action action, ResponseHandler handler) throws IOException {
        action.setActionId(this.actionIdFactory.getNext());
        toSend.put(action.getActionId(), handler);
        writer.send(action);
    }

    /**
     * @return Wartość true lub false informujący czy zostało nawiązane połączenie do asteriska.
     */
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
