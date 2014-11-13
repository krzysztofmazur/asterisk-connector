package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Events extends Action {
    private String eventMask;

    public Events(String eventMask) {
        this.eventMask = eventMask;
    }

    @Override
    protected String getMessage() {
        return "Action: Events\n"
                + "EventMask: " + eventMask + "\n";
    }
}
