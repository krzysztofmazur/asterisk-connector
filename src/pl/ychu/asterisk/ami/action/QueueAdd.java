package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class QueueAdd extends Action {
    private String queue;
    private String interfaceName;
    private String penalty;
    private String paused;
    private String memberName;
    private String stateInterface;

    public QueueAdd(String queue, String interfaceName, String penalty, String paused, String memberName, String stateInterface) {
        this.queue = queue;
        this.interfaceName = interfaceName;
        this.penalty = penalty;
        this.paused = paused;
        this.memberName = memberName;
        this.stateInterface = stateInterface;
    }

    @Override
    protected String getMessage() {
        return "Action: QueueAdd\n"
                + "Queue: " + queue + "\n"
                + "Interface: " + interfaceName + "\n"
                + (penalty != null ? "Penalty: " + penalty + "\n" : "")
                + (paused != null ? "Paused: " + paused + "\n" : "")
                + (memberName != null ? "MemberName: " + memberName + "\n" : "")
                + (stateInterface != null ? "StateInterface: " + stateInterface + "\n" : "");
    }
}
