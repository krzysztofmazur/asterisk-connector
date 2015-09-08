package pl.ychu.asterisk.manager.standard.action;

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
            return String.format("ActionID: %s\n", this.actionId);
        } else {
            return "";
        }
    }

    protected abstract String getMessage();

    public String toString() {
        return String.format("%s%s\n", this.getMessage(), this.getActionIdLine());
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }
}
