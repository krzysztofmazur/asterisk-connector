package pl.ychu.asterisk.ami;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class Event {

    private String eventMsg;

    protected Event(String event) {
        this.eventMsg = event;
        //TODO
    }

    public String getMessage() {
        return eventMsg;
    }

    //TODO
    @Deprecated
    public static Event parseEvent(String message) {
        Event e = new Event(message);
        return e;
    }
}
