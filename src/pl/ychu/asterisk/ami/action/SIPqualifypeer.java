package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class SIPqualifypeer extends Action {
    private String peer;

    public SIPqualifypeer(String peer) {
        this.peer = peer;
    }


    @Override
    protected String getMessage() {
        return "Action: SIPqualifypeer\n"
                + "Peer: " + peer + "\n";
    }
}
