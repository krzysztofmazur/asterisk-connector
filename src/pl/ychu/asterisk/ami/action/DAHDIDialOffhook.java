package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DAHDIDialOffhook extends Action {
    private String DAHDIChannel;
    private String number;

    public DAHDIDialOffhook(String DAHDIChannel, String number) {
        this.DAHDIChannel = DAHDIChannel;
        this.number = number;
    }

    @Override
    protected String getMessage() {
        return "Action: DAHDIDialOffhook\n"
                + "DAHDIChannel: " + DAHDIChannel + "\n"
                + "Number: " + number + "\n";
    }
}
