package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class PlayDTMF extends Action {
    private String channel;
    private String digit;

    public PlayDTMF(String channel, String digit) {
        this.channel = channel;
        this.digit = digit;
    }


    @Override
    protected String getMessage() {
        return "Action: PlayDTMF\n"
                + "Channel: " + channel + "\n"
                + "Digit: " + digit + "\n";
    }
}
