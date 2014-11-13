package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class AbsoluteTimeout extends Action {
    private String channel;
    private int timeout;

    public AbsoluteTimeout(String channel, int timeout) {
        this.channel = channel;
        this.timeout = timeout;
    }

    @Override
    protected String getMessage() {
        return "Action: AbsoluteTimeout\n"
                + "Channel: " + channel + "\n"
                + "Timeout: " + timeout + "\n";
    }
}
