package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class IAXnetstats extends Action {
    @Override
    public void setActionId(String actionId) {
        throw new UnsupportedOperationException("setActionId is not allowed for IAXnetstats AMI action");
    }


    @Override
    protected String getMessage() {
        return "Action IAXnetstats\n";
    }
}
