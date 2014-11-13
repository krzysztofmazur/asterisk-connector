package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueueStatus extends Action {
    private String queue;
    private String member;

    public QueueStatus(String queue, String member) {
        this.queue = queue;
        this.member = member;
    }


    @Override
    protected String getMessage() {
        return "Action: QueueStatus\n"
                + (queue != null ? "Queue: " + queue + "\n" : "")
                + (member != null ? "Member: " + member + "\n" : "");
    }
}
