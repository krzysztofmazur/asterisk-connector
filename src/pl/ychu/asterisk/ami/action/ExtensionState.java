package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class ExtensionState extends Action {
    private String exten;
    private String context;

    public ExtensionState(String exten, String context) {
        this.exten = exten;
        this.context = context;
    }

    @Override
    protected String getMessage() {
        return "Action: ExtensionState\n"
                + "Exten: " + exten + "\n"
                + "Context: " + context + "\n";
    }
}
