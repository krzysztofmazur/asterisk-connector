package pl.ychu.asterisk.manager.event;

import java.util.regex.Pattern;

public class EventProcessor<T> {
    private EventHandler<T> handler;
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

    public void setHandler(EventHandler<T> handler) {
        this.handler = handler;
    }

    public EventHandler<T> getHandler() {
        return handler;
    }

    public EventParser<T> getParser() {
        return parser;
    }

    public void processMessage(String message) {
        T event = parser.parse(message);
        handler.handle(event);
    }
}
