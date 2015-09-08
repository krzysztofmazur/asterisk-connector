package pl.ychu.asterisk.manager.standard.event;

public interface EventParser<T> {
    T parse(String message);
}
