package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueuePause extends Action {
    private String interfaceName;
    private String paused;
    private String queue;
    private String reson;

    public QueuePause(String interfaceName, String paused, String queue, String reson) {
        this.interfaceName = interfaceName;
        this.paused = paused;
        this.queue = queue;
        this.reson = reson;
    }

    @Override
    protected String getMessage() {
        return "Action: QueuePause\n"
                + "Interface: " + interfaceName + "\n"
                + "Paused: " + paused + "\n"
                + (queue != null ? "Queue: " + queue + "\n" : "")
                + (reson != null ? "Reason: " + reson + "\n" : "");
    }
}
