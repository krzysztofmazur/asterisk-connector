package pl.ychu.asterisk.manager.action;

import java.util.HashMap;

public class Response {
    private HashMap<String, String> variables;
    private String additionalData;

    public Response() {
        this.variables = new HashMap<>();
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public String getVariable(String variableName) {
        return variables.get(variableName);
    }

    public String getResponseStatus() {
        return this.getVariable("Response");
    }

    public String getActionId() {
        return this.getVariable("ActionID");
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public void addVariable(String name, String value) {
        this.variables.put(name, value);
    }
}
