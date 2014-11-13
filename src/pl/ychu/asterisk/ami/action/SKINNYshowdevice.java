package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class SKINNYshowdevice extends Action {
    private String device;

    public SKINNYshowdevice(String device) {
        this.device = device;
    }

    @Override
    protected String getMessage() {
        return "Action: SKINNYshowdevice\n"
                + "Device: " + device + "\n";
    }
}
