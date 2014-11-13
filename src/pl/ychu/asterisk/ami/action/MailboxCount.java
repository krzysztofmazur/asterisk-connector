package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class MailboxCount extends Action {
    private String mailbox;

    public MailboxCount(String mailbox) {
        this.mailbox = mailbox;
    }

    @Override
    protected String getMessage() {
        return "Action: MailboxCount\n"
                + "Mailbox: " + mailbox + "\n";
    }
}
