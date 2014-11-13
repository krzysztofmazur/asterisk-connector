package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Park extends Action {
    private String channel;
    private String channel2;
    private int timeout;
    private String parkinglot;

    public Park(String channel, String channel2, int timeout, String parkinglot) {
        this.channel = channel;
        this.channel2 = channel2;
        this.timeout = timeout;
        this.parkinglot = parkinglot;
    }


    @Override
    protected String getMessage() {
        return "Action: Park\n"
                + "Channel: " + channel + "\n"
                + "Channel2: " + channel2 + "\n"
                + (timeout != -1 ? "Timeout: " + timeout + "\n" : "")
                + (parkinglot != null ? "Parkinglot: " + parkinglot + "\n" : "");
    }
}
