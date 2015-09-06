package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.Writer;

import java.io.IOException;

public class PingThread implements Runnable {

    private Writer writer;
    private Connection connection;

    public PingThread(Connection connection) {
        this.connection = connection;
        this.writer = connection.getWriter();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(connection.getConnectionConfiguration().getReadTimeout() / 2);
            } catch (InterruptedException ex) {
                break;
            }
            try {
                if (writer != null && connection.isConnected()) {
                    writer.send(new Action("Ping"));
                }
            } catch (IOException ignored) {
            }
        }
    }
}
