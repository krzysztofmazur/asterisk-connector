package pl.ychu.asterisk.ami;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by krzysztof on 15.11.14.
 */
public class CustomAction extends Action {

    private String actionName;
    private HashMap<String, String> variables;

    public CustomAction(String actionName) {
        this.actionName = actionName;
        variables = new HashMap<String, String>();
    }

    public void addVariable(String name, String value) {
        variables.put(name, value);
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    @Override
    protected String getMessage() {
        String message = "Action: " + actionName + "\n";
        Iterator i = variables.entrySet().iterator();
        while (i.hasNext()) {
            HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) i.next();
            message += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return message;
    }
}
