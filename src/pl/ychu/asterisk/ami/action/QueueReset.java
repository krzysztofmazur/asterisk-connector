package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueueReset extends Action {
    private String queue;

    public QueueReset() {
        this(null);
    }

    public QueueReset(String queue) {
        this.queue = queue;
    }

    @Override
    protected String getMessage() {
        return "Action: QueueReset\n"
                + (queue != null ? "Queue: " + queue + "\n" : "");
    }
}
