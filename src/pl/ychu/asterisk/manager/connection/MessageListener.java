package pl.ychu.asterisk.manager.connection;

public interface MessageListener {
    void processMessage(String message);

    void setConnection(Connection connection);
}
