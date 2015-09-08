package pl.ychu.asterisk.manager.connection;

public interface MessageListener {
    void onIncomingMessage(String message);

    void onOutgoingMessage(String message);
}
