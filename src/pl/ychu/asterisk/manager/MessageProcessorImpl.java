package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.connection.Reader;
import pl.ychu.asterisk.manager.event.Event;
import pl.ychu.asterisk.manager.event.EventProcessor;
import pl.ychu.asterisk.manager.event.StandardEventParser;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class MessageProcessorImpl implements MessageProcessor {
    private final Pattern eventPattern;
    private final Pattern responsePattern;
    private final Pattern actionIdPattern;
    private final HashMap<String, ResponseHandler> responseHandlers;
    private Reader reader;
    private final Object mutex;

    private EventProcessorRepository eventProcessorRepository;
    private ResponseHandler defaultResponseHandler;

    public MessageProcessorImpl() {
        this.responseHandlers = new HashMap<>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^.*(Response:).*");
        this.actionIdPattern = Pattern.compile("^.*(ActionID:).*");
        this.mutex = new Object();

        eventProcessorRepository = new EventProcessorRepository();
    }

    public void addEventProcessor(EventProcessor eventProcessor) {
        eventProcessorRepository.addEventProcessor(eventProcessor);
    }

    public void setDefaultResponseHandler(ResponseHandler defaultResponseHandler) {
        this.defaultResponseHandler = defaultResponseHandler;
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
        for (EventProcessor eventProcessor : eventProcessorRepository.getMatchedProcessors(message)) {
            eventProcessor.processMessage(message);
        }
    }

    private void processResponse(String message) {
        Response r = new Response(message);
        ResponseHandler handler = responseHandlers.get(r.getActionId());
        if (handler != null) {
            new Thread(new ResponseAsyncHelper(r, handler)).start();
        } else if (defaultResponseHandler != null) {
            new Thread(new ResponseAsyncHelper(r, defaultResponseHandler)).start();
        }
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

    private class EventProcessorRepository {
        private List<EventProcessor> list;

        public EventProcessorRepository() {
            list = new LinkedList<>();
        }

        public List<EventProcessor> getMatchedProcessors(String message) {
            List<EventProcessor> result = new ArrayList<>();
            for (EventProcessor processor : list) {
                if (processor.getPattern().matcher(message).find()) {
                    result.add(processor);
                }
            }
            return result;
        }

        public void addEventProcessor(EventProcessor eventProcessor) {
            list.add(eventProcessor);
        }
    }
}
