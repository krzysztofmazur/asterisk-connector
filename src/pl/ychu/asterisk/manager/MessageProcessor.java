package pl.ychu.asterisk.manager;

import java.io.IOException;

public interface MessageProcessor {
    public void processMessage() throws IOException;

    public void addHandler(EventHandler handler);

    public void removeHandler(EventHandler handler);

    public void addResponseHandler(String actionId, ResponseHandler responseHandler);

    public void removeResponseHandler(ResponseHandler responseHandler);

    public void setReader(Reader reader);
}
