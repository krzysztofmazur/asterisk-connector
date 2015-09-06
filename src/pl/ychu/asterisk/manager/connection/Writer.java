package pl.ychu.asterisk.manager.connection;

import pl.ychu.asterisk.manager.action.AbstractAction;

import java.io.IOException;
import java.io.OutputStream;

public class Writer {

    private OutputStream os;

    protected Writer() {
    }

    public Writer(OutputStream os) {
        this.os = os;
    }

    public synchronized void send(AbstractAction action) throws IOException {
        os.write(action.getBytes());
    }

    public synchronized void close() throws IOException {
        os.close();
    }
}
