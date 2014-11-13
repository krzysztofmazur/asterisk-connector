package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueueRemove extends Action {
    private String queue;
    private String interfaceName;

    public QueueRemove(String queue, String interfaceName) {
        this.queue = queue;
        this.interfaceName = interfaceName;
    }

    @Override
    protected String getMessage() {
        return "Action: QueueRemove\n"
                + "Queue: " + queue + "\n"
                + "Interface: " + interfaceName + "\n";
    }
}
