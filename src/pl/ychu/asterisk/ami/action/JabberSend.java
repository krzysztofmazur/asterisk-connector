package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class JabberSend extends Action {
    private String jabber;
    private String JID;
    private String message;

    public JabberSend(String jabber, String JID, String message) {
        this.jabber = jabber;
        this.JID = JID;
        this.message = message;
    }

    @Override
    protected String getMessage() {
        return "Action: JabberSend\n"
                + "Jabber: " + jabber + "\n"
                + "JID: " + JID + "\n"
                + "Messag: " + message + "\n";
    }
}
