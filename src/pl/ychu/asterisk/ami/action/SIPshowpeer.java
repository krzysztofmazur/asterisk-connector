package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class SIPshowpeer extends Action {
    private String peer;

    public SIPshowpeer(String peer) {
        this.peer = peer;
    }

    @Override
    protected String getMessage() {
        return "Action: SIPqualifypeer\n"
                + "Peer: " + peer + "\n";
    }
}
