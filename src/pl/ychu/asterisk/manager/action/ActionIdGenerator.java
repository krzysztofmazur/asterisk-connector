package pl.ychu.asterisk.manager.action;

public class ActionIdGenerator {
    private long actionId;

    public ActionIdGenerator() {
        actionId = 0;
    }

    public String getNext() {
        synchronized (this) {
            this.actionId++;
            return String.format("%s%09d", System.currentTimeMillis(), this.actionId);
        }
    }
}
