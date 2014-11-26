package pl.ychu.asterisk.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SynchronizedMessageProcessor implements MessageProcessor {
    private final Pattern eventPattern;
    private final Pattern responsePattern;
    private final Pattern actionIdPattern;
    private final ArrayList<EventHandler> handlers;
    private final HashMap<String, ResponseHandler> responseHandlers;
    private Reader reader;
    private final Object mutex;

    public SynchronizedMessageProcessor() {
        this.handlers = new ArrayList<EventHandler>();
        this.responseHandlers = new HashMap<String, ResponseHandler>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^.*(Response:).*");
        this.actionIdPattern = Pattern.compile("^.*(ActionID:).*");
        this.mutex = new Object();
    }

    @Override
    public void processMessage() throws IOException {
        String message = reader.readMessage();
        if (responsePattern.matcher(message).find() || actionIdPattern.matcher(message).find()) {
            processResponse(message);
        } else if (eventPattern.matcher(message).find()) {
            processEvent(message);
        }
    }

    private void processEvent(String message) {
        UnifiedEvent e = UnifiedEvent.parseEvent(message);
        synchronized (mutex) {
            for (EventHandler handler : handlers) {
                handler.handleEvent(e);
            }
        }
    }

    private void processResponse(String message) {
        Response r = new Response(message);
        ResponseHandler handler = responseHandlers.get(r.getActionId());
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
    public void removeResponseHandler(ResponseHandler responseHandler) {
        responseHandlers.remove(responseHandler);
    }

    @Override
    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
