package pl.ychu.asterisk.manager;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public interface EventHandler {
    public void handleEvent(Event unifiedEvent);
    public void handleResponse(Response response);
}
