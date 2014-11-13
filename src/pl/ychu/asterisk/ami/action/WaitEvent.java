package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class WaitEvent extends Action {
    private int timeout;

    public WaitEvent(int timeout) {
        this.timeout = timeout;
    }

    @Override
    protected String getMessage() {
        return "Action: WaitEvent\n"
                + "Timeout: " + timeout + "\n";
    }
}
