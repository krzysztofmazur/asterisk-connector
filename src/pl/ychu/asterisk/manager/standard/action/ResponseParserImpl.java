package pl.ychu.asterisk.manager.standard.action;

public class ResponseParserImpl implements ResponseParser {
    @Override
    public Response parse(String message) {
        Response response = new Response();
        StringBuilder sb = new StringBuilder(1000);
        for (String line : message.split("\n")) {
            if (line.endsWith("\r") && line.length() != 0) {
                String[] lineS = line.split(":", 2);
                if (lineS.length == 2) {
                    response.addVariable(lineS[0].trim(), lineS[1].trim());
                }
            } else {
                sb.append(line).append("\n");
            }
        }
        if (sb.length() != 0) {
            response.setAdditionalData(sb.toString());
        }
        return response;
    }
}
