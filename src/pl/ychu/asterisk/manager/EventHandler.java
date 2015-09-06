package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.event.Event;

public interface EventHandler {
    void handleEvent(Event event);
    void handleResponse(Response response);
}
