package pl.ychu.asterisk.manager.action;

public class ActionIdGenerator implements ActionIdGeneratorInterface {
    private long actionId;

    public ActionIdGenerator() {
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
