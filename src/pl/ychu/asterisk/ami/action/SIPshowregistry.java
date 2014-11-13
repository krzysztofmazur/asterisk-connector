package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class SIPshowregistry extends Action {
    @Override
    protected String getMessage() {
        return "Action: SIPshowregistry\n";
    }
}
