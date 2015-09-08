package pl.ychu.asterisk.manager.standard.event;

import java.util.regex.Pattern;

public class EventProcessor<T> {
    private EventListener<T> listener;
    private EventParser<T> parser;
    private Pattern pattern;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setParser(EventParser<T> parser) {
        this.parser = parser;
    }

    public void setListener(EventListener<T> listener) {
        this.listener = listener;
    }

    public EventListener<T> getListener() {
        return listener;
    }

    public EventParser<T> getParser() {
        return parser;
    }

    public void processMessage(String message) {
        T event = this.getParser().parse(message);
        this.getListener().onEvent(event);
    }
}
