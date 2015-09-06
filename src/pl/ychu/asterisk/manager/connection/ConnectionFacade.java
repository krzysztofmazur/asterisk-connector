package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.*;
import pl.ychu.asterisk.manager.action.AbstractAction;
import pl.ychu.asterisk.manager.action.Action;
import pl.ychu.asterisk.manager.action.ActionIdGenerator;
import pl.ychu.asterisk.manager.action.ResponseHandler;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;

public class ConnectionFacade {
    private final Connection connection;
    private final ActionIdGenerator actionIdFactory;
    private ConnectionConfiguration connectionConfiguration;
    private Thread mainThread;
    private Writer writer;
    private boolean working;
    private MessageProcessor msgProcessor;

    public ConnectionFacade(Connection connection, ConnectionConfiguration connectionConfiguration) {
        this.connection = connection;
        this.connectionConfiguration = connectionConfiguration;
        this.actionIdFactory = new ActionIdGenerator();
        this.createThread();

    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.msgProcessor = messageProcessor;
    }

    public void sendAction(AbstractAction action) throws IOException {
        writer.send(action);
    }

    public void sendAction(AbstractAction action, ResponseHandler handler) throws IOException, NotConnectedException {
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
    }

    public void stop() throws NotConnectedException {
        if (!working) {
            throw new NotConnectedException("Not connected to asterisk.");
        }
        mainThread.interrupt();
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
                            msgProcessor.processMessage(connection.getReader().readMessage());
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
}
