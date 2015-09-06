package pl.ychu.asterisk.manager.event;

public interface EventParser<T> {
    T parse(String message);
}
