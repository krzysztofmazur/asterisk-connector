package pl.ychu.asterisk.manager.connection;

import java.io.IOException;
import java.io.OutputStream;

public class Writer {

    private OutputStream os;

    protected Writer() {
    }

    public Writer(OutputStream os) {
        this.os = os;
    }

    public synchronized void send(String action) throws IOException {
        this.os.write(action.getBytes());
    }

    public synchronized void close() throws IOException {
        this.os.close();
    }
}
