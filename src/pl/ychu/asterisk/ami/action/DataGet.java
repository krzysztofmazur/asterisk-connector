package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class DataGet extends Action {
    private String path;
    private String search;
    private String filter;

    public DataGet(String path, String search, String filter) {
        this.path = path;
        this.search = search;
        this.filter = filter;
    }

    @Override
    protected String getMessage() {
        return "Action: DataGet\n"
                + "Path: " + path + "\n"
                + (search != null ? "Search: " + search + "\n" : "")
                + (filter != null ? "Filter: " + filter + "\n" : "");
    }
}
