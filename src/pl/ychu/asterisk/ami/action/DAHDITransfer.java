package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DAHDITransfer extends Action {
    private String DAHDIChannel;

    public DAHDITransfer(String DAHDIChannel) {
        this.DAHDIChannel = DAHDIChannel;
    }

    @Override
    protected String getMessage() {
        return "Action: DAHDITransfer\n"
                + "DAHDIChannel: " + DAHDIChannel + "\n";
    }
}
