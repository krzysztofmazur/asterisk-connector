package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Getvar extends Action {
    private String channel;
    private String variable;

    public Getvar(String variable) {
        this(null, variable);
    }

    public Getvar(String channel, String variable) {
        this.channel = channel;
        this.variable = variable;
    }

    @Override
    protected String getMessage() {
        return "Action: Getval\n"
                + (channel != null ? "Channel: " + channel + "\n" : "")
                + "Variable: " + variable + "\n";
    }
}
