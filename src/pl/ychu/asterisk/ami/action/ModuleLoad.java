package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class ModuleLoad extends Action {
    private String module;
    private String loadType;

    public ModuleLoad(String loadType) {
        this(null, loadType);
    }

    public ModuleLoad(String module, String loadType) {
        this.module = module;
        this.loadType = loadType;
    }

    @Override
    protected String getMessage() {
        return "Action: ModuleLoad\n"
                + (module != null ? "Module: " + module + "\n" : "")
                + "LoadType: " + loadType + "\n";
    }
}
