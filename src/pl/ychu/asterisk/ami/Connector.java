package pl.ychu.asterisk.ami;

import pl.ychu.asterisk.AsteriskConfiguration;
import pl.ychu.asterisk.ami.action.Command;
import pl.ychu.asterisk.ami.action.ListCommands;
import pl.ychu.asterisk.ami.action.Login;
import pl.ychu.asterisk.ami.action.Ping;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

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

    protected Connector() {

    }

    public Connector(AsteriskConfiguration configuration) throws IOException {
        this.configuration = configuration;
        this.handlers = new ArrayList<EventHandler>();
        this.client = new Socket();
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
                        System.out.print(message);
                        if (message.contains("Success")) {
                            while (running) {
                                message = reader.readMessage();
                                System.out.print(message);
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

    public static void main(String[] args) throws IOException, InterruptedException {
        AsteriskConfiguration conf = new AsteriskConfiguration("192.168.24.13", 5038, "admin", "holi!holi9");
        Connector conn = new Connector(conf);
        conn.start();
        Thread.sleep(3000);
        conn.sendAction(new Command("queue show"));
    }
}
