package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;

/**
 * Created by Krzysztof on 2014-11-16.
 */
public class AsynchronizedConnection {
    private final Connection connection;
    private final Object mutex;
    private final ActionId actionIdFactory;
    private Thread mainThread;
    private Thread maintainingThread;
    private Writer writer;
    private Reader reader;
    private boolean working;
    private boolean enabledMaintainingThread = true;
    private final MessageProcessor msgProcessor;

    public AsynchronizedConnection(Connection connection, MessageProcessor msgProcessor) {
        this.connection = connection;
        this.msgProcessor = msgProcessor;
        this.mutex = new Object();
        this.actionIdFactory = new ActionId();
        this.createThread();
        this.createMaintainingThread();

    }

    public AsynchronizedConnection(Connection connection, MessageProcessor msgProcessor, EventHandler eventHandler) {
        this(connection, msgProcessor);
        this.addHandler(eventHandler);
    }

    public void enableMaintainingThread(boolean enabled) {
        this.enabledMaintainingThread = enabled;
    }

    public void sendAction(Action action) throws IOException {
        writer.send(action);
    }

    public void addHandler(EventHandler handler) {
        msgProcessor.addHandler(handler);
    }

    public void removeHandler(EventHandler handler) {
        msgProcessor.removeHandler(handler);
    }

    public void sendAction(Action action, ResponseHandler handler) throws IOException, NotConnectedException {
        if (!working) {
            throw new NotConnectedException("Not connected to asterisk.");
        }
        action.setActionId(this.actionIdFactory.getNext());
        msgProcessor.addResponseHandler(action.getActionId(), handler);
        writer.send(action);
    }

    public void start() throws IOException, NotAuthorizedException {
        reconnect();
        mainThread.start();
        if (this.enabledMaintainingThread) {
            maintainingThread.start();
        }
    }

    public void stop() throws NotConnectedException {
        if (!working) {
            throw new NotConnectedException("Not connected to asterisk.");
        }
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
        msgProcessor.setReader(reader);
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
                            msgProcessor.processMessage();
                        }
                    } catch (IOException ex) {
                        working = false;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex2) {
                            break;
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
        maintainingThread = new Thread(new ConnectionMaintainingThread(connection));
    }
}
