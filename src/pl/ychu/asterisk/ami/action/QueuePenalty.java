package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueuePenalty extends Action {
    private String interfaceName;
    private String penalty;
    private String queue;

    public QueuePenalty(String interfaceName, String penalty, String queue) {
        this.interfaceName = interfaceName;
        this.penalty = penalty;
        this.queue = queue;
    }


    @Override
    protected String getMessage() {
        return "Action: QueuePenalty\n"
                + "Interface: " + interfaceName + "\n"
                + "Penalty: " + penalty + "\n"
                + (queue != null ? "Queue: " + queue + "\n" : "");
    }
}
