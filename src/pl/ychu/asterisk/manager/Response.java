package pl.ychu.asterisk.manager;

import java.util.HashMap;

public class Response {
    private HashMap<String, String> vars;
    private String addrInfo;
    private String message;

    public Response(String message) {
        this.message = message;
        this.parse();
    }

    protected void parse()
    {
        StringBuilder sb = new StringBuilder(1000);
        this.vars = new HashMap<>();
        for (String line : message.split("\n")) {
            if (line.endsWith("\r") && line.length() != 0) {
                String[] lineS = line.split(":", 2);
                if (lineS.length == 2) {
                    vars.put(lineS[0].trim(), lineS[1].trim());
                }
            } else {
                sb.append(line);
                sb.append("\n");
            }
        }
        if (sb.length() != 0) {
            addrInfo = sb.toString();
        }
    }

    public String getMessage() {
        return message;
    }

    public String getAdditionalData() {
        return addrInfo;
    }

    public String getVariable(String variableName) {
        return vars.get(variableName);
    }

    public String getResponseStatus() {
        return this.getVariable("Response");
    }

    public String getActionId() {
        return this.getVariable("ActionID");
    }

}
