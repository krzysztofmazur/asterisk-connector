package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-12.
 */
public final class ListCommands extends Action {

    @Override
    protected String getMessage() {
        return "Action: ListCommands\n";
    }
}
