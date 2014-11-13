package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class GetConfigJSON extends Action {

    private String filename;
    private String category;

    public GetConfigJSON(String filename) {
        this(filename, null);
    }

    public GetConfigJSON(String filename, String category) {
        this.filename = filename;
        this.category = category;
    }

    @Override
    protected String getMessage() {
        return "Action: GetConfigJSON\n"
                + "Filename: " + filename + "\n"
                + (category != null ? "Category: " + category + "\n" : "");
    }
}
