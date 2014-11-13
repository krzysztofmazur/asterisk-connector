package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class ListCategories extends Action {
    private String filename;

    public ListCategories(String filename) {
        this.filename = filename;
    }

    @Override
    protected String getMessage() {
        return "Action: ListCategories\n"
                + "Filename: " + filename + "\n";
    }
}
