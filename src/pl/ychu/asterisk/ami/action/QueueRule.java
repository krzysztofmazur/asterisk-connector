package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class QueueRule extends Action {
    private String rule;

    public QueueRule(String rule) {
        this.rule = rule;
    }

    @Override
    protected String getMessage() {
        return "Action: QueueRule\n"
                + (rule != null ? "Rule: " + rule + "\n" : "");
    }
}
