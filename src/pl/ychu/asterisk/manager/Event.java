package pl.ychu.asterisk.manager;

import java.util.HashMap;

public class Event {

    protected final String eventMsg;
    public String eventName;
    private final HashMap<String, String> map;

    protected Event(String event) {
        this.eventMsg = event;
        this.map = new HashMap<>();
        this.parse();
    }

    public void parse()
    {
        for (String line : this.eventMsg.split("\n")) {
            String[] lineS = line.split(":", 2);
            if (lineS[0].contains("Event")) {
                this.setEventName(lineS[1].trim());
            }
            if (line.equals("") || lineS.length == 1) {
                continue;
            }
            map.put(lineS[0].trim(), lineS[1].trim());
        }
    }

    public String getVariable(String variableName) {
        return map.get(variableName);
    }

    public static Event parseEvent(String message) {
        Event e = new Event(message);
        return e;
    }

    protected void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getMessage() {
        return eventMsg;
    }

    public String getEventName() {
        return eventName;
    }
}
