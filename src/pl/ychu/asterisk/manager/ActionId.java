package pl.ychu.asterisk.manager;

public class ActionId {
    private long actionId;

    public ActionId() {
        actionId = 0;
    }

    public String getNext() {
        synchronized (this) {
            this.actionId++;
            return System.currentTimeMillis() + String.format("%09d", this.actionId);
        }
    }
}
