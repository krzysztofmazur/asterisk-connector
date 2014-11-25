package pl.ychu.asterisk.ami;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Krzysztof on 2014-11-25.
 */
public class SynchronizedMessageProcessor implements MessageProcessor {
    private final Pattern eventPattern;
    private final Pattern responsePattern;
    private final ArrayList<EventHandler> handlers;
    private final HashMap<String, ResponseHandler> responseHandlers;
    private Reader reader;
    private final Object mutex;

    public SynchronizedMessageProcessor() {
        this.handlers = new ArrayList<EventHandler>();
        this.responseHandlers = new HashMap<String, ResponseHandler>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^(Response:).*");
        this.mutex = new Object();
    }

    @Override
    public void processMessage() throws IOException {
        String message = reader.readMessage();
        if (eventPattern.matcher(message).find()) {
            processEvent(message);
        } else if (responsePattern.matcher(message).find()) {
            processResponse(message);
        }
    }

    private void processEvent(String message) {
        Event e = Event.parseEvent(message);
        synchronized (mutex) {
            for (EventHandler handler : handlers) {
                handler.handleEvent(e);
            }
        }
    }

    private void processResponse(String message) {
        Response r = new Response(message);
        ResponseHandler handler = responseHandlers.remove(r.getActionId());
        if (handler != null) {
            handler.handleResponse(r);
        } else {
            synchronized (mutex) {
                for (EventHandler evHandler : handlers) {
                    evHandler.handleResponse(r);
                }
            }
        }
    }

    @Override
    public void addHandler(EventHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void removeHandler(EventHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public void addResponseHandler(String actionId, ResponseHandler responseHandler) {
        responseHandlers.put(actionId, responseHandler);
    }

    @Override
    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
