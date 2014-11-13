package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class UnpauseMonitor extends Action {
    private String channel;

    public UnpauseMonitor(String channel) {
        this.channel = channel;
    }

    @Override
    protected String getMessage() {
        return "Action: UnpauseMonitor\n"
                + "Channel: " + channel + "\n";
    }
}
