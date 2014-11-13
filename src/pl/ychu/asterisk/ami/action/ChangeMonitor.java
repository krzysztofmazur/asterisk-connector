package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class ChangeMonitor extends Action {
    private String channel;
    private String file;

    public ChangeMonitor(String channel, String file) {
        this.channel = channel;
        this.file = file;
    }

    @Override
    protected String getMessage() {
        return "Action: ChangeMonitor\n"
                + "Channel: " + channel + "\n"
                + "File: " + file + "\n";
    }
}
