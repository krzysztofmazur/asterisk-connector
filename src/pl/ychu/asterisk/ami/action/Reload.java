package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class Reload extends Action {
    private String module;

    public Reload() {
        this(null);
    }

    public Reload(String module) {
        this.module = module;
    }

    @Override
    protected String getMessage() {
        return "Action: Reload\n"
                + (module != null ? "Module: " + module + "\n" : "");
    }
}
