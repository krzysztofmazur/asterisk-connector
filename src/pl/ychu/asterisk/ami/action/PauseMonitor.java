package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class PauseMonitor extends Action {
    private String channel;

    public PauseMonitor(String channel) {
        this.channel = channel;
    }

    @Override
    protected String getMessage() {
        return "Action: PauseMonitor\n"
                + "Channel: " + channel + "\n";
    }
}
