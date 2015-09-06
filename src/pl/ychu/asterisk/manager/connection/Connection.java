package pl.ychu.asterisk.manager.connection;

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

    public void connect() throws IOException {
        client = new Socket();
        client.setSoTimeout(readTimeout * 2);
        client.connect(new InetSocketAddress(
                hostName,
                hostPort
        ), connectionTimeout);

        reader = new Reader(client.getInputStream());
        writer = new Writer(client.getOutputStream());
    }

    public void close() throws IOException {
        reader.close();
        writer.close();
        client.close();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public Reader getReader() {
        return reader;
    }

    public Writer getWriter() {
        return writer;
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
}
