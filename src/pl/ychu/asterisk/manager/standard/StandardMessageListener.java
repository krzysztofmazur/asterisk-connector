package pl.ychu.asterisk.manager.standard;

import pl.ychu.asterisk.manager.standard.action.*;
import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.MessageListener;
import pl.ychu.asterisk.manager.standard.event.EventProcessor;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class StandardMessageListener implements MessageListener {

    private Connection connection;

    private final Pattern eventPattern;
    private final Pattern responsePattern;
    private final Pattern actionIdPattern;

    private ActionIdGeneratorInterface actionIdGenerator;

    private EventProcessorRepository eventProcessorRepository;
    private HashMap<String, ResponseListener> responseHandlers;
    private ResponseListener defaultResponseListener;
    private ResponseParser responseParser;

    public StandardMessageListener() {
        this.responseHandlers = new HashMap<>();
        this.eventPattern = Pattern.compile("^(Event:).*");
        this.responsePattern = Pattern.compile("^.*(Response:).*");
        this.actionIdPattern = Pattern.compile("^.*(ActionID:).*");

        eventProcessorRepository = new EventProcessorRepository();
    }

    public void addEventProcessor(EventProcessor eventProcessor) {
        eventProcessorRepository.addEventProcessor(eventProcessor);
    }

    public void setDefaultResponseListener(ResponseListener defaultResponseListener) {
        this.defaultResponseListener = defaultResponseListener;
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
            this.processResponse(message);
        } else if (eventPattern.matcher(message).find()) {
            this.processEvent(message);
        }
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void processEvent(String message) {
        for (EventProcessor eventProcessor : this.eventProcessorRepository.getMatchedProcessors(message)) {
            eventProcessor.processMessage(message);
        }
    }

    private void processResponse(String message) {
        if (this.responseParser != null) {
            Response r = this.responseParser.parse(message);
            ResponseListener handler = this.responseHandlers.get(r.getActionId());
            if (handler != null) {
                handler.onResponse(r);
            } else if (this.defaultResponseListener != null) {
                this.defaultResponseListener.onResponse(r);
            }
        }
    }

    public void sendAction(AbstractAction action) throws IOException, NotConnectedException {
        if (!this.connection.isConnected()) {
            throw new NotConnectedException("Not connected to asterisk.");
        }
        this.connection.getWriter().send(action.toString());
    }

    public void sendAction(AbstractAction action, ResponseListener handler) throws IOException, NotConnectedException {
        if (this.actionIdGenerator == null) {
            throw new IllegalStateException("Set ActionIdGenerator before you send action with response handler.");
        }
        this.responseHandlers.put(action.getActionId(), handler);
        this.sendAction(action);
    }

    private class EventProcessorRepository {
        private List<EventProcessor> eventProcessorList;

        public EventProcessorRepository() {
            this.eventProcessorList = new LinkedList<>();
        }

        public List<EventProcessor> getMatchedProcessors(String message) {
            List<EventProcessor> result = new ArrayList<>();
            for (EventProcessor processor : this.eventProcessorList) {
                if (processor.getPattern().matcher(message).find()) {
                    result.add(processor);
                }
            }
            return result;
        }

        public void addEventProcessor(EventProcessor eventProcessor) {
            this.eventProcessorList.add(eventProcessor);
        }
    }
}
