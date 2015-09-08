package pl.ychu.asterisk.manager.standard.event;

public class StandardEventParser implements EventParser<Event> {
    @Override
    public Event parse(String message) {
        Event event = new Event();

        for (String line : message.split("\n")) {
            String[] lineS = line.split(":", 2);
            if (line.contains("Event:")) {
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
