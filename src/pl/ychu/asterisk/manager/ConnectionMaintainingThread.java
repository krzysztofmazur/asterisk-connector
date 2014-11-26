package pl.ychu.asterisk.manager;

import java.io.IOException;

public class ConnectionMaintainingThread implements Runnable {

    private Writer writer;
    private Connection connection;

    public ConnectionMaintainingThread(Connection connection) {
        this.connection = connection;
        this.writer = connection.getWriter();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(connection.getReadTimeout() / 2);
            } catch (InterruptedException ex) {
                break;
            }
            try {
                if (writer != null && connection.isConnected()) {
                    writer.send(new UnifiedAction("Ping"));
                }
            } catch (IOException ex) {
            }
        }
    }
}
