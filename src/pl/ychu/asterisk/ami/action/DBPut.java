package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DBPut extends Action {
    private String family;
    private String key;
    private String val;

    public DBPut(String family, String key) {
        this(family, key, null);
    }

    public DBPut(String family, String key, String val) {
        this.family = family;
        this.key = key;
        this.val = val;
    }

    @Override
    protected String getMessage() {
        return "Action: DBPut\n"
                + "Family: " + family + "\n"
                + "Key: " + key + "\n"
                + (val != null ? "Val: " + val + "\n" : "");
    }
}
