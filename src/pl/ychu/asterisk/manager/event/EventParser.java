package pl.ychu.asterisk.manager.event;

public interface EventParser {
    Event parse(String message);
}
