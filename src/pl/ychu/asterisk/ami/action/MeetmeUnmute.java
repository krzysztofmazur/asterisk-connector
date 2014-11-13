package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class MeetmeUnmute extends Action {
    private String meetme;
    private String usernum;

    public MeetmeUnmute(String meetme, String usernum) {
        this.meetme = meetme;
        this.usernum = usernum;
    }

    @Override
    protected String getMessage() {
        return "Action: MeetmeUnmute\n"
                + "Meetme: " + meetme + "\n"
                + "Usernum: " + usernum + "\n";
    }
}
