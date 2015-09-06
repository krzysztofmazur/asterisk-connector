package pl.ychu.asterisk.manager.event;

public class EventProcessor<T> {
    private EventHandler<T> handler;
    private EventParser<T> parser;

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
}
