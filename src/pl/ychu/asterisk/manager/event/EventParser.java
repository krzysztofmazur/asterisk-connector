package pl.ychu.asterisk.manager.event;

import java.util.regex.Pattern;

public interface EventParser<T> {
    Pattern getPattern();

    T parse(String message);
}
