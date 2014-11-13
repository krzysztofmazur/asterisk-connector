package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class SKINNYshowline extends Action {
    private String line;

    public SKINNYshowline(String line) {
        this.line = line;
    }

    @Override
    protected String getMessage() {
        return "Action: SKINNYshowline\n"
                + "Line: " + line + "\n";
    }
}
