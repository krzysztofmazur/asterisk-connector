package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Atxfer extends Action {
    private String channel;
    private String exten;
    private String context;
    private int priority;

    public Atxfer(String channel, String exten, String context, int priority) {
        this.channel = channel;
        this.exten = exten;
        this.context = context;
        this.priority = priority;
    }

    @Override
    protected String getMessage() {
        return "Action: Atxfer\n"
                + "Channel: " + channel + "\n"
                + "Exten: " + exten + "\n"
                + "Context: " + context + "\n"
                + "Priority: " + priority + "\n";
    }
}
