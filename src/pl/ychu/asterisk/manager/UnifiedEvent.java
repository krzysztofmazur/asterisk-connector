package pl.ychu.asterisk.manager;

import java.util.HashMap;

public class UnifiedEvent extends Event {

    private final HashMap<String, String> map;

    protected UnifiedEvent(String event) {
        super(event);
        this.map = new HashMap<String, String>();
        for (String line : event.split("\n")) {
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

    public static UnifiedEvent parseEvent(String message) {
        UnifiedEvent e = new UnifiedEvent(message);
        return e;
    }
}
