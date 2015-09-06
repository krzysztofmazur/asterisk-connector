package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.Action;

import java.io.IOException;
import java.io.OutputStream;

public class Writer {

    private OutputStream os;

    protected Writer() {
    }

    public Writer(OutputStream os) {
        this.os = os;
    }

    public void send(Action action) throws IOException {
        synchronized (this) {
            os.write(action.getBytes());
        }
    }

    public void close() throws IOException {
        os.close();
    }
}
