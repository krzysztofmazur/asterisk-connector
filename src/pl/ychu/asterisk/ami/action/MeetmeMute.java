package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class MeetmeMute extends Action {
    private String meetme;
    private String usernum;

    public MeetmeMute(String meetme, String usernum) {
        this.meetme = meetme;
        this.usernum = usernum;
    }

    @Override
    protected String getMessage() {
        return "Action: MeetmeMute\n"
                + "Meetme: " + meetme + "\n"
                + "Usernum: " + usernum + "\n";
    }
}
