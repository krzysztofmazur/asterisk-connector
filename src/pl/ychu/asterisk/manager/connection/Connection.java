package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.UnifiedAction;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {
    private Socket client;
    private ConnectionConfiguration connectionConfiguration;
    private boolean connected = false;
    private Reader reader;
    private Writer writer;

    public Connection(ConnectionConfiguration connectionConfiguration) {
        this.connectionConfiguration = connectionConfiguration;
    }

    public void connect() throws IOException, NotAuthorizedException {
        client = new Socket();
        client.setSoTimeout(connectionConfiguration.getReadTimeout() * 2);
        client.connect(new InetSocketAddress(
                connectionConfiguration.getHostName(),
                connectionConfiguration.getHostPort()
        ), connectionConfiguration.getConnectTimeout());

        reader = new Reader(client.getInputStream());
        writer = new Writer(client.getOutputStream());

        UnifiedAction l = new UnifiedAction("Login");
        l.putVariable("Username", connectionConfiguration.getUserName());
        l.putVariable("Secret", connectionConfiguration.getUserPassword());
        if (!connectionConfiguration.isListeningEvents()) {
            l.putVariable("Events", "off");
        }
        writer.send(l);
        if (!reader.readMessage().contains("Success")) {
            throw new NotAuthorizedException("Bad user name or secret.");
        }
    }

    public void close() throws IOException {
        reader.close();
        writer.close();
        client.close();
    }

    public boolean isConnected() {
        return connected;
    }

    public Reader getReader() {
        return reader;
    }

    public Writer getWriter() {
        return writer;
    }

    public ConnectionConfiguration getConnectionConfiguration() {
        return this.connectionConfiguration;
    }
}
