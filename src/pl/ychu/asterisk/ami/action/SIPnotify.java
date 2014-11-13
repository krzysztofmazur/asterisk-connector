package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class SIPnotify extends Action {
    private String channel;
    private String variable;

    public SIPnotify(String channel, String variable) {
        this.channel = channel;
        this.variable = variable;
    }


    @Override
    protected String getMessage() {
        return "Action: SIPnotify\n"
                + "Channel: " + channel + "\n"
                + "Variable:" + variable + "\n";
    }
}
