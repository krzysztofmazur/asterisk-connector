package pl.ychu.asterisk.ami;

/**
 * Created by Krzysztof on 2014-11-11.
 */
abstract public class Action {
    protected String actionId;

    protected abstract String getMessage();

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    private String getActionIdLine() {
        if (this.actionId != null) {
            return "ActionID: " + actionId + "\n";
        } else {
            return "";
        }

    }

    @Override
    public String toString() {
        return this.getMessage() + this.getActionIdLine() + "\n";
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }
}
