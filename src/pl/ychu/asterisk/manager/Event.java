package pl.ychu.asterisk.manager;

abstract public class Event {

    public String eventName;

    protected void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
