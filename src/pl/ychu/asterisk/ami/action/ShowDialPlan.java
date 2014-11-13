package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class ShowDialPlan extends Action {
    private String extension;
    private String context;

    public ShowDialPlan(String extension, String context) {
        this.extension = extension;
        this.context = context;
    }


    @Override
    protected String getMessage() {
        return "Action: ShowDialPlan\n"
                + (extension != null ? "Extension: " + extension + "\n" : "")
                + (context != null ? "Context: " + context + "\n" : "");
    }
}
