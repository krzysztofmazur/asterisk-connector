package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DAHDIHangup extends Action {
    private String DAHDIChannel;

    public DAHDIHangup(String DAHDIChannel) {
        this.DAHDIChannel = DAHDIChannel;
    }

    @Override
    protected String getMessage() {
        return "Action: DAHDIHangup\n"
                + "DAHDIChannel: " + DAHDIChannel + "\n";
    }
}
