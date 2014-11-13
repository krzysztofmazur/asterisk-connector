package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DBDel extends Action {
    private String family;
    private String key;

    public DBDel(String family, String key) {
        this.family = family;
        this.key = key;
    }

    @Override
    protected String getMessage() {
        return "Action: DBDel\n"
                + "Family: " + family + "\n"
                + "Key: " + key + "\n";
    }
}
