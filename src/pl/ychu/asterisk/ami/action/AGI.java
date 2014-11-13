package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class AGI extends Action {

    private String channel;
    private String command;
    private String commandId;

    public AGI(String channel, String command) {
        this(channel, command, null);
    }

    public AGI(String channel, String command, String commandId) {
        this.channel = channel;
        this.command = command;
        this.commandId = commandId;
    }

    @Override
    protected String getMessage() {
        String action = "Action: AGI\n"
                + "Channel: " + channel + "\n"
                + "Command: " + command + "\n";
        if (commandId != null) {
            action += "CommandID: " + commandId + "\n";
        }
        return action;
    }
}
