package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class UserEvent extends Action {
    private String userEvent;
    private HashMap<String, String> headers;

    public UserEvent(String userEvent, HashMap<String, String> headers) {
        this.userEvent = userEvent;
        this.headers = headers;
    }

    @Override
    protected String getMessage() {
        String msg = "Action: UserEvent\n"
                + "UserEvent: " + userEvent + "\n";
        for (String key : headers.keySet()) {
            msg += key + ": " + headers.get(key) + "\n";
        }
        return msg;
    }
}
