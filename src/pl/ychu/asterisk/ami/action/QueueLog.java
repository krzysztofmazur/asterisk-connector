package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueueLog extends Action {
    private String queue;
    private String event;
    private String uniqueid;
    private String interfaceName;
    private String message;

    public QueueLog(String queue, String event, String uniqueid, String interfaceName, String message) {
        this.queue = queue;
        this.event = event;
        this.uniqueid = uniqueid;
        this.interfaceName = interfaceName;
        this.message = message;
    }


    @Override
    protected String getMessage() {
        return "Action: QueueLog\n"
                + "Queue: " + queue + "\n"
                + "Event: " + event + "\n"
                + (uniqueid != null ? "Uniqueid: " + uniqueid + "\n" : "")
                + (interfaceName != null ? "Interface: " + interfaceName + "\n" : "")
                + (message != null ? "Message: " + message + "\n" : "");
    }
}
