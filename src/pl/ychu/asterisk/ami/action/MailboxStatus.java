package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class MailboxStatus extends Action {
    private String mailbox;

    public MailboxStatus(String mailbox) {
        this.mailbox = mailbox;
    }

    @Override
    protected String getMessage() {
        return "Action: MailboxStatus\n"
                + "Mailbox: " + mailbox + "\n";
    }
}
