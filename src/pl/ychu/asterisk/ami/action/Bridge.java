package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Bridge extends Action {
    private String channel1;
    private String channel2;
    private String tone;

    public Bridge(String channel1, String channel2) {
        this(channel1, channel2, null);
    }

    public Bridge(String channel1, String channel2, String tone) {
        this.channel1 = channel1;
        this.channel2 = channel2;
        this.tone = tone;
    }

    @Override
    protected String getMessage() {
        return "Action: Bridge\n"
                + "Channel1: " + channel1 + "\n"
                + "Channel2: " + channel2 + "\n"
                + (tone != null ? "Tone: " + tone + "\n" : "");
    }
}
