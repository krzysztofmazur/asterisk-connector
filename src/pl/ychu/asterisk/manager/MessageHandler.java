package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.connection.Connection;

public interface MessageHandler {
    void processMessage(String message);

    void setConnection(Connection connection);
}
