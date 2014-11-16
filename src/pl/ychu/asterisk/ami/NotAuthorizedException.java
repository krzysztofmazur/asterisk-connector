package pl.ychu.asterisk.ami;

/**
 * Created by krzysztof on 16.11.14.
 */
public class NotAuthorizedException extends Exception {
    public NotAuthorizedException(String s) {
        super(s);
    }
}
