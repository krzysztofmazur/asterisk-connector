package pl.ychu.asterisk.ami;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface MessageProcessor {
    public void processMessage() throws IOException;

    public void addHandler(EventHandler handler);

    public void removeHandler(EventHandler handler);

    public void addResponseHandler(String actionId, ResponseHandler responseHandler);

    public void setReader(Reader reader);
}
