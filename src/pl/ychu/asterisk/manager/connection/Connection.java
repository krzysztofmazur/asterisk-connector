package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.exception.NotAuthorizedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {
    protected String hostName = "127.0.0.1";
    protected int hostPort = 5038;
    protected int connectionTimeout = 5000;
    protected int readTimeout = 30000;

    protected Socket client;
    protected Reader reader;
    protected Writer writer;
    protected Thread thread;

    protected MessageHandler messageHandler;
    protected MessageListener messageListener;

    protected String loginAction;

    public Connection() {
        this.createThread();
    }

    public void connect(String loginAction) throws IOException, NotAuthorizedException {
        this.client = new Socket();
        this.client.setSoTimeout(this.readTimeout * 2);
        this.client.connect(new InetSocketAddress(
                hostName,
                hostPort
        ), this.connectionTimeout);

        this.reader = new Reader(client.getInputStream());
        this.writer = new Writer(client.getOutputStream());

        this.login(loginAction);
        this.thread.start();
    }

    protected void login(String loginAction) throws IOException, NotAuthorizedException {
        this.loginAction = loginAction;
        this.writer.send(loginAction);
        if (!this.reader.readMessage().contains("Success")) {
            throw new NotAuthorizedException("Bad user name or secret.");
        }
    }

    public void close() throws IOException {
        this.thread.interrupt();
        this.reader.close();
        this.writer.close();
        this.client.close();
    }

    public boolean isConnected() {
        return this.client.isConnected();
    }

    public String readMessage() throws IOException {
        return this.reader.readMessage();
    }

    public void sendMessage(String message) throws IOException {
        this.writer.send(message);
        if (messageListener != null) {
            this.messageListener.onOutgoingMessage(message);
        }
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        messageHandler.setConnection(this);
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    protected void createThread() {
        this.thread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (!isConnected()) {
                            connect(loginAction);
                        }
                        while (!Thread.currentThread().isInterrupted()) {
                            String message = readMessage();
                            if (messageHandler != null) {
                                messageHandler.processMessage(message);
                            }
                            if (messageListener != null) {
                                messageListener.onIncomingMessage(message);
                            }
                        }
                    } catch (IOException ex) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex2) {
                            break;
                        }
                    } catch (NotAuthorizedException ignore) {
                        break;
                    }
                }
            }
        };
    }
}
