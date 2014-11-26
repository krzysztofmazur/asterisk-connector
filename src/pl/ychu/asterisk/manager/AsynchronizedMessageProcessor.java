package pl.ychu.asterisk.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Krzysztof on 2014-11-25.
 */
public class AsynchronizedMessageProcessor implements MessageProcessor {
    private final Pattern eventPattern;
    private final Pattern responsePattern;
    private final ArrayList<EventHandler> handlers;
    private final HashMap<String, ResponseHandler> responseHandlers;
    private Reader reader;
    private final Object mutex;

    public AsynchronizedMessageProcessor() {
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
        UnifiedEvent e = UnifiedEvent.parseEvent(message);
        synchronized (mutex) {
            for (EventHandler handler : handlers) {
                new Thread(new EventAsyncHelper(e, handler)).start();
            }
        }
    }

    private void processResponse(String message) {
        Response r = new Response(message);
        ResponseHandler handler = responseHandlers.remove(r.getActionId());
        if (handler != null) {
            new Thread(new ResponseAsyncHelper(r, handler)).start();
        } else {
            synchronized (mutex) {
                for (EventHandler evHandler : handlers) {
                    new Thread(new DefaultResponseAsyncHelper(r, evHandler)).start();
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

    private class ResponseAsyncHelper implements Runnable {

        private Response response;
        private ResponseHandler handler;

        public ResponseAsyncHelper(Response response, ResponseHandler handler) {
            this.response = response;
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.handleResponse(response);
        }
    }

    private class DefaultResponseAsyncHelper implements Runnable {
        private Response response;
        private EventHandler handler;

        public DefaultResponseAsyncHelper(Response response, EventHandler handler) {
            this.response = response;
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.handleResponse(response);
        }
    }

    private class EventAsyncHelper implements Runnable {
        private UnifiedEvent unifiedEvent;
        private EventHandler handler;

        public EventAsyncHelper(UnifiedEvent unifiedEvent, EventHandler handler) {
            this.unifiedEvent = unifiedEvent;
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.handleEvent(unifiedEvent);
        }
    }
}
