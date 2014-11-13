package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class Setvar extends Action {
    private String channel;
    private String variable;
    private String value;

    public Setvar(String variable, String value) {
        this(null, variable, value);
    }

    public Setvar(String channel, String variable, String value) {
        this.channel = channel;
        this.variable = variable;
        this.value = value;
    }

    @Override
    protected String getMessage() {
        return "Action: Setvar\n"
                + (channel != null ? "Channel: " + channel + "\n" : "")
                + "Variable: " + variable + "\n"
                + "Value: " + value + "\n";
    }
}
