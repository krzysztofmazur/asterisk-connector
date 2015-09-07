package pl.ychu.asterisk.manager.connection;

public interface MessageHandler {
    void processMessage(String message);

    void setConnection(Connection connection);
}
