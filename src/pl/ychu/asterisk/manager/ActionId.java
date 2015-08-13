package pl.ychu.asterisk.manager;

public class ActionId {
    private long actionId;

    public ActionId() {
        actionId = 0;
    }

    public String getNext() {
        synchronized (this) {
            this.actionId++;
            return String.format("%s%09d", System.currentTimeMillis(), this.actionId);
        }
    }
}
