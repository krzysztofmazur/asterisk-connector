package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class MixMonitorMute extends Action {

    private String channel;
    private String direction;
    private String state;

    public MixMonitorMute(String channel, String direction, String state) {
        this.channel = channel;
        this.direction = direction;
        this.state = state;
    }

    @Override
    protected String getMessage() {
        return "Action: MixMonitorMute\n"
                + "Channel: " + channel + "\n"
                + (direction != null ? "Direction: " + direction + "\n" : "")
                + (state != null ? "State: " + state + "\n" : "");
    }
}
