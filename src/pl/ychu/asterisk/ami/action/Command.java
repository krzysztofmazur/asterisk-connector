package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-12.
 */
public final class Command extends Action {

    private String command;

    protected Command() {
    }

    public Command(String command) {
        this.command = command;
    }

    @Override
    protected String getMessage() {
        return "Action: Command\n"
                + "Command: " + this.command + "\n";
    }
}
