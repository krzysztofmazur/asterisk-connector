package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DAHDIDNDon extends Action {
    private String DAHDIChannel;

    public DAHDIDNDon(String DAHDIChannel) {
        this.DAHDIChannel = DAHDIChannel;
    }

    @Override
    protected String getMessage() {
        return "Action: DAHDIDNDon\n"
                + "DAHDIChannel: " + DAHDIChannel + "\n";
    }
}
