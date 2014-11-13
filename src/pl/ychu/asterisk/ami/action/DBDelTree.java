package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DBDelTree extends Action {
    private String family;
    private String key;

    public DBDelTree(String family) {
        this(family, null);
    }

    public DBDelTree(String family, String key) {
        this.family = family;
        this.key = key;
    }

    @Override
    protected String getMessage() {
        return "Action: DBDelTree\n"
                + "Family: " + family + "\n"
                + (key != null ? "Key: " + key + "\n" : "");
    }
}
