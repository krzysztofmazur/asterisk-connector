package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class GetConfig extends Action {

    private String filename;
    private String category;

    public GetConfig(String filename) {
        this(filename, null);
    }

    public GetConfig(String filename, String category) {
        this.filename = filename;
        this.category = category;
    }

    @Override
    protected String getMessage() {
        return "Action: GetConfig\n"
                + "Filename: " + filename + "\n"
                + (category != null ? "Category: " + category + "\n" : "");
    }
}
