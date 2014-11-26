package pl.ychu.asterisk.manager;

/**
 * Created by Krzysztof on 2014-11-26.
 */
abstract public class Response {

    private String message;

    public Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
