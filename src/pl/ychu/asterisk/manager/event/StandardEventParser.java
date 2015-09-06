package pl.ychu.asterisk.manager.event;

import java.util.regex.Pattern;

public class StandardEventParser implements EventParser<Event> {

    private Pattern pattern;

    @Override
    public synchronized Pattern getPattern() {
        if (pattern == null) {
            pattern = Pattern.compile("^.*");
        }
        return pattern;
    }

    @Override
    public Event parse(String message) {
        Event event = new Event();

        for (String line : message.split("\n")) {
            String[] lineS = line.split(":", 2);
            if (lineS[0].contains("Event")) {
                event.setEventName(lineS[1].trim());
            }
            if (line.equals("") || lineS.length == 1) {
                continue;
            }
            event.putVariable(lineS[0].trim(), lineS[1].trim());
        }
        return event;
    }
}
