package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class SendText extends Action {
    private String channel;
    private String message;

    public SendText(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    @Override
    protected String getMessage() {
        return "Action: SendText\n"
                + "Channel: " + channel + "\n"
                + "Message: " + message + "\n";
    }
}
