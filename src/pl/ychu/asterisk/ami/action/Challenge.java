package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Challenge extends Action {
    @Override
    protected String getMessage() {
        return "Action: Challenge\n";
    }
}
