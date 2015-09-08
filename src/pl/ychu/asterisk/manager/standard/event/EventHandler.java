package pl.ychu.asterisk.manager.standard.event;

public interface EventHandler<T> {
    void handle(T event);
}
