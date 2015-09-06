package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.ConnectionConfiguration;
import pl.ychu.asterisk.manager.connection.Writer;

import java.io.IOException;

public class PingThread implements Runnable {

    private Connection connection;
    private ConnectionConfiguration connectionConfiguration;

    public PingThread(Connection connection, ConnectionConfiguration connectionConfiguration) {
        this.connection = connection;
        this.connectionConfiguration = connectionConfiguration;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(connectionConfiguration.getReadTimeout() / 2);
            } catch (InterruptedException ex) {
                break;
            }
            try {
                if (connection.isConnected()) {
                    connection.getWriter().send(new Action("Ping"));
                }
            } catch (IOException ignored) {
            }
        }
    }
}
