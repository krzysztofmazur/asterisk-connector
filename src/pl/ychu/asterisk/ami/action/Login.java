package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-11.
 */
public final class Login extends Action {

    private String userName;
    private String password;
    private boolean events;

    public Login(String userName, String password, boolean events) {
        this.userName = userName;
        this.password = password;
        this.events = events;
    }

    @Override
    protected String getMessage() {
        return "Action: Login\n"
                + "Username: " + userName + "\n"
                + "Secret: " + password + "\n"
                + (events ? "" : "Events: off\n");
    }
}
