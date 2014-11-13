package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class ModuleCheck extends Action {
    private String module;

    public ModuleCheck(String module) {
        this.module = module;
    }

    @Override
    public void setActionId(String actionId) {
        throw new UnsupportedOperationException("setActionId is not allowed in ModuleCheck AMI action");
    }

    @Override
    protected String getMessage() {
        return "Action: ModuleCheck\n"
                + "Module: " + module + "\n";
    }
}
