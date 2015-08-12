package pl.ychu.asterisk.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UnifiedAction extends Action {

    private String actionName;
    private List<Variable> variableList;

    public UnifiedAction(String actionName) {
        this.actionName = actionName;
        variableList = new ArrayList<Variable>();
    }

    public void putVariable(String name, String value) {
        variableList.add(new Variable(name, value));
    }

    public void removeVariable(String name) {
        for (Variable variable : variableList) {
            if (variable.getName().equals(name))
                variableList.remove(variable);
        }
    }

    @Override
    protected String getMessage() {
        String message = "Action: " + actionName + "\n";
        for (Variable variable : variableList) {
            message += variable.getName() + ": " + variable.getValue() + "\n";
        }
        return message;
    }

    class Variable {
        private String name;
        private String value;

        public Variable(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
