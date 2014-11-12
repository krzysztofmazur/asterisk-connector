package pl.ychu.asterisk.ami;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class ActionId {
    private long actionId;
    private Object mutex;

    public ActionId() {
        actionId = 0;
        mutex = new Object();
    }

    public String getNext() {
        synchronized (mutex) {
            this.actionId++;
            return System.currentTimeMillis() + String.format("%09d", this.actionId);
        }
    }
}
