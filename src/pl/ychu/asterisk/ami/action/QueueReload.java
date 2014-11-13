package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueueReload extends Action {
    private String queue;
    private boolean members;
    private boolean rules;
    private boolean parameters;

    public QueueReload(String queue, boolean members, boolean rules, boolean parameters) {
        this.queue = queue;
        this.members = members;
        this.rules = rules;
        this.parameters = parameters;
    }

    @Override
    protected String getMessage() {
        return "Action: QueueReload\n"
                + (queue != null ? "Queue: " + queue + "\n" : "")
                + "Members: " + (members ? "yes" : "no") + "\n"
                + "Rules: " + (rules ? "yes" : "no") + "\n"
                + "Parameters: " + (parameters ? "yes" : "no") + "\n";
    }
}
