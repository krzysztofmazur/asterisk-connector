package pl.ychu.asterisk.ami;

import org.omg.CORBA.WrongTransaction;
import pl.ychu.asterisk.ami.action.Login;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 * Created by Krzysztof on 2014-11-16.
 */
public class Connection {
    private Socket client;
    private Configuration configuration;
    private boolean connected = false;
    private Reader reader;
    private Writer writer;
    private int readTimeout = 30000;
    private int connectTimeout = 5000;

    public Connection(Configuration configuration) {
        this.configuration = configuration;
    }

    public void connect() throws IOException, NotAuthorizedException {
        Pattern loginPattern = Pattern.compile("^(Response: Success).*");
        client = new Socket();
        client.setSoTimeout(readTimeout * 2);
        client.connect(new InetSocketAddress(configuration.getHostName(), configuration.getHostPort()), connectTimeout);
        reader = new Reader(client.getInputStream());
        writer = new Writer(client.getOutputStream());
        Login l = new Login(configuration.getUserName(), configuration.getUserPassword(), configuration.isListeningEvents());
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

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
}
