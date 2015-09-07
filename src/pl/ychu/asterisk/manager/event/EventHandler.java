package pl.ychu.asterisk.manager.event;

public interface EventHandler<T> {
    void handle(T event);
}
