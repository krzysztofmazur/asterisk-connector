package pl.ychu.asterisk.manager;

import java.util.ArrayList;
import java.util.List;

public class Action {

    protected String actionId;
    private String actionName;
    private List<Variable> variableList;

    public Action(String actionName) {
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

    protected String getMessage() {
        String message = "Action: " + actionName + "\n";
        for (Variable variable : variableList) {
            message += variable.getName() + ": " + variable.getValue() + "\n";
        }
        return message;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    private String getActionIdLine() {
        if (this.actionId != null) {
            return "ActionID: " + actionId + "\n";
        } else {
            return "";
        }

    }

    @Override
    public String toString() {
        return this.getMessage() + this.getActionIdLine() + "\n";
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
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
