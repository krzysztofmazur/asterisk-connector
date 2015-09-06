package pl.ychu.asterisk.manager.event;

import java.util.HashMap;
import java.util.Map;

public class Event {
    public String eventName;
    private final Map<String, String> map;

    public Event() {
        this.map = new HashMap<>();
    }

    public String getVariable(String variableName) {
        return map.get(variableName);
    }

    public Map<String, String> getVariableMap() {
        return map;
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
