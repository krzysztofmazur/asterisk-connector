package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.connection.Reader;

import java.io.IOException;

public interface MessageProcessor {
    void processMessage(String message) throws IOException;

    void addResponseHandler(String actionId, ResponseHandler responseHandler);

    void removeResponseHandler(ResponseHandler responseHandler);

    void setReader(Reader reader);
}
