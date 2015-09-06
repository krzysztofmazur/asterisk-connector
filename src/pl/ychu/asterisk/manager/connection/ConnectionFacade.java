package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.*;
import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.Writer;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;

public class ConnectionFacade {
    private final Connection connection;
    private final ActionIdGenerator actionIdFactory;
    private ConnectionConfiguration connectionConfiguration;
    private Thread mainThread;
    private Thread pingThread;
    private Writer writer;
    private boolean working;
    private boolean enablePingThread = true;
    private MessageProcessor msgProcessor;

    public ConnectionFacade(Connection connection, ConnectionConfiguration connectionConfiguration) {
        this.connection = connection;
        this.connectionConfiguration = connectionConfiguration;
        this.actionIdFactory = new ActionIdGenerator();
        this.createThread();
        this.createPingThread();

    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.msgProcessor = messageProcessor;
    }

    public void enablePingThread(boolean enabled) {
        this.enablePingThread = enabled;
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
        if (this.enablePingThread) {
            pingThread.start();
        }
    }

    public void stop() throws NotConnectedException {
        if (!working) {
            throw new NotConnectedException("Not connected to asterisk.");
        }
        mainThread.interrupt();
        if (this.enablePingThread && pingThread.isAlive()) {
            pingThread.interrupt();
        }
    }

    private void reconnect() throws IOException, NotAuthorizedException {
        connection.connect();
        Action loginAction = new Action("Login");
        loginAction.putVariable("Username", connectionConfiguration.getUserName());
        loginAction.putVariable("Secret", connectionConfiguration.getUserPassword());
        working = true;
        writer = connection.getWriter();
        writer.send(loginAction);
        if (!connection.getReader().readMessage().contains("Success")) {
            throw new NotAuthorizedException("Bad user name or secret.");
        }
        msgProcessor.setReader(connection.getReader());
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

    private void createPingThread() {
        pingThread = new Thread(new PingThread(connection, connectionConfiguration));
    }
}
