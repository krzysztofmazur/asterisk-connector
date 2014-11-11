package pl.ychu.asterisk.ami;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Krzysztof on 2014-11-11.
 */
public class Writer {

    private OutputStream os;

    protected Writer() {
    }

    public Writer(OutputStream os) {
        this.os = os;
    }

    public void send(Action action) throws IOException {
        os.write(action.getBytes());
    }
}
