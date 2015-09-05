package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.Reader;
import pl.ychu.asterisk.manager.connection.Writer;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;

import java.io.IOException;

public class SynchronizedConnection {
    private final Connection connection;
    private final Object mutex;
    private final ActionId actionIdFactory;
    private Writer writer;
    private Reader reader;
    private boolean working;
    private boolean enabledMaintainingThread = true;
    private final MessageProcessor msgProcessor;

    public SynchronizedConnection(Connection connection, MessageProcessor msgProcessor) {
        this.connection = connection;
        this.msgProcessor = msgProcessor;
        this.mutex = new Object();
        this.actionIdFactory = new ActionId();

    }

    public SynchronizedConnection(Connection connection, MessageProcessor msgProcessor, EventHandler eventHandler) {
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

    private void reconnect() throws IOException, NotAuthorizedException {
        connection.connect();
        working = true;
        writer = connection.getWriter();
        reader = connection.getReader();
        msgProcessor.setReader(reader);
    }

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
}
