package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DAHDIShowChannels extends Action {
    private String DAHDIChannel;

    public DAHDIShowChannels() {
        this(null);
    }

    public DAHDIShowChannels(String DAHDIChannel) {
        this.DAHDIChannel = DAHDIChannel;
    }

    @Override
    protected String getMessage() {
        return "Action: DAHDIShowChannels\n"
                + (DAHDIChannel != null ? "DAHDIChannel: " + DAHDIChannel + "\n" : "");
    }
}
