package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DAHDIRestart extends Action {
    private String DAHDIChannel;

    public DAHDIRestart(String DAHDIChannel) {
        this.DAHDIChannel = DAHDIChannel;
    }

    @Override
    protected String getMessage() {
        return "Action: DAHDIRestart\n"
                + "DAHDIChannel: " + DAHDIChannel + "\n";
    }
}
