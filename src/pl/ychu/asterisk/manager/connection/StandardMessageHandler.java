package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.action.*;
import pl.ychu.asterisk.manager.event.EventProcessor;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class StandardMessageHandler implements MessageHandler {

    private Connection connection;

    private final Pattern eventPattern;
    private final Pattern responsePattern;
    private final Pattern actionIdPattern;

    private ActionIdGeneratorInterface actionIdGenerator;

    private EventProcessorRepository eventProcessorRepository;
    private HashMap<String, ResponseHandler> responseHandlers;
    private ResponseHandler defaultResponseHandler;
    private ResponseParser responseParser;

    public StandardMessageHandler() {
        this.responseHandlers = new HashMap<>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^.*(Response:).*");
        this.actionIdPattern = Pattern.compile("^.*(ActionID:).*");

        eventProcessorRepository = new EventProcessorRepository();
    }

    public void addEventProcessor(EventProcessor eventProcessor) {
        eventProcessorRepository.addEventProcessor(eventProcessor);
    }

    public void setDefaultResponseHandler(ResponseHandler defaultResponseHandler) {
        this.defaultResponseHandler = defaultResponseHandler;
    }

    public void setResponseParser(ResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public void setActionIdGenerator(ActionIdGeneratorInterface actionIdGenerator) {
        this.actionIdGenerator = actionIdGenerator;
    }

    @Override
    public void processMessage(String message) {
        if (responsePattern.matcher(message).find() || actionIdPattern.matcher(message).find()) {
            processResponse(message);
        } else if (eventPattern.matcher(message).find()) {
            processEvent(message);
        }
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void processEvent(String message) {
        for (EventProcessor eventProcessor : eventProcessorRepository.getMatchedProcessors(message)) {
            eventProcessor.processMessage(message);
        }
    }

    private void processResponse(String message) {
        if (responseParser != null) {
            Response r = responseParser.parse(message);
            ResponseHandler handler = responseHandlers.get(r.getActionId());
            if (handler != null) {
                handler.handleResponse(r);
            } else if (defaultResponseHandler != null) {
                defaultResponseHandler.handleResponse(r);
            }
        }
    }

    public void sendAction(AbstractAction action) throws IOException, NotConnectedException {
        if (!connection.isConnected()) {
            throw new NotConnectedException("Not connected to asterisk.");
        }
        connection.getWriter().send(action);
    }

    public void sendAction(AbstractAction action, ResponseHandler handler) throws IOException, NotConnectedException {
        if (this.actionIdGenerator == null) {
            throw new IllegalStateException("Set ActionIdGenerator before you send action with response handler.");
        }
        responseHandlers.put(action.getActionId(), handler);
        this.sendAction(action);
    }

    private class EventProcessorRepository {
        private List<EventProcessor> eventProcessorList;

        public EventProcessorRepository() {
            eventProcessorList = new LinkedList<>();
        }

        public List<EventProcessor> getMatchedProcessors(String message) {
            List<EventProcessor> result = new ArrayList<>();
            for (EventProcessor processor : eventProcessorList) {
                if (processor.getPattern().matcher(message).find()) {
                    result.add(processor);
                }
            }
            return result;
        }

        public void addEventProcessor(EventProcessor eventProcessor) {
            eventProcessorList.add(eventProcessor);
        }
    }
}
