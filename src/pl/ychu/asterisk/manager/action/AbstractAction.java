package pl.ychu.asterisk.manager.action;

public abstract class AbstractAction {

    protected String actionId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    protected String getActionIdLine() {
        if (this.actionId != null) {
            return "ActionID: " + actionId + "\n";
        } else {
            return "";
        }
    }

    protected abstract String getMessage();

    public String toString() {
        return this.getMessage() + this.getActionIdLine() + "\n";
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }
}
