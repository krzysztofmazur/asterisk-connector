package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueueSummary extends Action {
    private String queue;

    public QueueSummary(String queue) {
        this.queue = queue;
    }

    @Override
    protected String getMessage() {
        return "Action: QueueSummary\n"
                + (queue != null ? "Queue: " + queue + "\n" : "");
    }
}
