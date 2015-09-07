package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.MessageHandler;
import pl.ychu.asterisk.manager.action.AbstractAction;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {
    private String hostName = "127.0.0.1";
    private int hostPort = 5038;
    private int connectionTimeout = 5000;
    private int readTimeout = 30000;

    private Socket client;
    private Reader reader;
    private Writer writer;
    private Thread thread;

    private MessageHandler messageHandler;
    private AbstractAction loginAction;

    public Connection() {
        this.createThread();
    }

    public void connect(AbstractAction loginAction) throws IOException, NotAuthorizedException {
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

    private void login(AbstractAction loginAction) throws IOException, NotAuthorizedException {
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

    public Reader getReader() {
        return this.reader;
    }

    public Writer getWriter() {
        return this.writer;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        messageHandler.setConnection(this);
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

    private void createThread() {
        this.thread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (!isConnected()) {
                            connect(loginAction);
                        }
                        while (!Thread.currentThread().isInterrupted()) {
                            if (messageHandler != null) {
                                messageHandler.processMessage(reader.readMessage());
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
