package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DBGet extends Action {
    private String family;
    private String key;

    public DBGet(String family, String key) {
        this.family = family;
        this.key = key;
    }

    @Override
    protected String getMessage() {
        return "Action: DBGet\n"
                + "Family: " + family + "\n"
                + "Key: " + key + "\n";
    }
}
