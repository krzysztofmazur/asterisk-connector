package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class AgentLogoff extends Action {

    private String agent;
    private boolean soft;

    public AgentLogoff(String agent, boolean soft) {
        this.agent = agent;
        this.soft = soft;
    }

    @Override
    protected String getMessage() {
        return "Action: AgentLogoff\n"
                + "Agent: " + agent + "\n"
                + (soft ? "Soft: true" : "Soft: false") + "\n";
    }
}
