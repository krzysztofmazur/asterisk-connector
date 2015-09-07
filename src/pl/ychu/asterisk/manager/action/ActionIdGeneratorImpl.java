package pl.ychu.asterisk.manager.action;

public class ActionIdGeneratorImpl implements ActionIdGenerator {
    private long actionId;

    public ActionIdGeneratorImpl() {
        actionId = 0;
    }

    @Override
    public String getNext() {
        synchronized (this) {
            this.actionId++;
            return String.format("%s%09d", System.currentTimeMillis(), this.actionId);
        }
    }
}
