package pl.ychu.asterisk.ami;

import java.util.HashMap;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Response {
    private String message;
    private HashMap<String, String> vars;
    private String addrInfo;
    private String actionId;

    //TODO
    public Response(String message) {
        this.message = message;
        String[] msgSplited = this.message.split("\n");
        StringBuilder sb = new StringBuilder(1000);
        this.vars = new HashMap<String, String>();
        for (String line : msgSplited) {
            if (line.endsWith("\r") && line.length() != 0) {
                String[] lineSplited = line.split(":", 2);
                if (lineSplited.length == 2) {
                    vars.put(lineSplited[0].trim(), lineSplited[1].trim());
                    if (lineSplited[0].equals("ActionID")) {
                        actionId = lineSplited[1].trim();
                    }
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

    public HashMap<String, String> getVars() {
        return vars;
    }

    public String getAddrInfo() {
        return addrInfo;
    }

    public String getActionId() {
        return actionId;
    }
}
