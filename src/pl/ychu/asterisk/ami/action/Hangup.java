package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Hangup extends Action {
    private String channel;
    private int cause;

    public Hangup(String channel) {
        this(channel, -1);
    }

    public Hangup(String channel, int cause) {
        this.channel = channel;
        this.cause = cause;
    }

    @Override
    protected String getMessage() {
        return "Action: Hangup\n"
                + "Channel: " + channel + "\n"
                + (cause != -1 ? "Cause: " + cause + "\n" : "");
    }
}
