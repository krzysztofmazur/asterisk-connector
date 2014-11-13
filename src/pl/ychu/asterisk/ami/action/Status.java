package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class Status extends Action {
    private String channel;
    private String variables;

    public Status(String channel, String variables) {
        this.channel = channel;
        this.variables = variables;
    }

    public Status(String channel) {
        this(channel, null);
    }

    @Override
    protected String getMessage() {
        return "Action: Status\n"
                + "Channel: " + channel + "\n"
                + (variables != null ? "Variables: " + variables + "\n" : "");
    }
}
