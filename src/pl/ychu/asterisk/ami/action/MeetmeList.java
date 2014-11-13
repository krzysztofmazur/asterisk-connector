package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class MeetmeList extends Action {

    private String conference;

    public MeetmeList() {
        this(null);
    }

    public MeetmeList(String conference) {
        this.conference = conference;
    }

    @Override
    protected String getMessage() {
        return "Action: MeetmeList\n"
                + (conference != null ? "Conference: " + conference + "\n" : "");
    }
}
