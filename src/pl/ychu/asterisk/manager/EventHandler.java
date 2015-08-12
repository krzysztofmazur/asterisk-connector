package pl.ychu.asterisk.manager;

public interface EventHandler {
    public void handleEvent(Event event);
    public void handleResponse(Response response);
}
