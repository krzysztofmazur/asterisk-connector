package pl.ychu.asterisk.manager.event;

import java.util.HashMap;

public class Event {
    public String eventName;
    private final HashMap<String, String> map;

    public Event() {
        this.map = new HashMap<>();
    }

    public String getVariable(String variableName) {
        return map.get(variableName);
    }

    public void putVariable(String variableName, String variableValue) {
        map.put(variableName, variableValue);
    }

    protected void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
