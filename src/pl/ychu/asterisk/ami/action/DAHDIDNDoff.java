package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DAHDIDNDoff extends Action {
    private String DAHDIChannel;

    public DAHDIDNDoff(String DAHDIChannel) {
        this.DAHDIChannel = DAHDIChannel;
    }

    @Override
    protected String getMessage() {
        return "Action: DAHDIDNDoff\n"
                + "DAHDIChannel: " + DAHDIChannel + "\n";
    }
}
