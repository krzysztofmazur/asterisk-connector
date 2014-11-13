package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class LocalOptimizeAway extends Action {
    private String channel;

    public LocalOptimizeAway(String channel) {
        this.channel = channel;
    }

    @Override
    protected String getMessage() {
        return "Action: LocalOptimizeAway\n"
                + "Channel: " + channel + "\n";
    }
}
