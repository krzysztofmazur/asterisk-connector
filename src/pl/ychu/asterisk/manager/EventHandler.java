package pl.ychu.asterisk.manager;

public interface EventHandler {
    void handleEvent(Event event);
    void handleResponse(Response response);
}
