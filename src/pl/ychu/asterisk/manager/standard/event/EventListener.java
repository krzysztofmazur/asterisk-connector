package pl.ychu.asterisk.manager.standard.event;

public interface EventListener<T> {
    void onEvent(T event);
}
