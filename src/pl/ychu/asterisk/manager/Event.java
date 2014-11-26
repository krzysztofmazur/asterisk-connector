package pl.ychu.asterisk.manager;

import java.util.HashMap;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class Event {

    private final String eventMsg;
    private final HashMap<String, String> map;

    protected Event(String event) {
        this.eventMsg = event;
        this.map = new HashMap<String, String>();
        for (String line : event.split("\n")) {
            String[] lineS = line.split(":", 2);
            if (line.equals("") || lineS.length == 1) {
                continue;
            }
            map.put(lineS[0].trim(), lineS[1].trim());
        }
    }

    public String getMessage() {
        return eventMsg;
    }

    public String getEventName() {
        return this.getVariable("Event");
    }

    public String getVariable(String variableName) {
        return map.get(variableName);
    }

    public static Event parseEvent(String message) {
        Event e = new Event(message);
        return e;
    }
}
