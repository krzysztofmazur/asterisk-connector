package pl.ychu.asterisk.manager;

abstract public class Event {

    protected final String eventMsg;
    public String eventName;

    protected Event(String eventMsg) {
        this.eventMsg = eventMsg;
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
