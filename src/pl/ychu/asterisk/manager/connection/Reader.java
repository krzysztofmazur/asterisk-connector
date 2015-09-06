package pl.ychu.asterisk.manager.connection;

import java.io.IOException;
import java.io.InputStream;

public class Reader {

    private InputStream is;

    protected Reader() {
    }

    public Reader(InputStream is) {
        this.is = is;
    }

    public String readMessage() throws IOException {
        StringBuilder sb = new StringBuilder(200);
        char last;
        while (true) {
            last = (char) is.read();
            sb.append(last);
            if (sb.length() > 3) {
                int tmpPos = sb.length();
                if (sb.charAt(tmpPos - 3) == '\n' && sb.charAt(tmpPos - 2) == '\r') {
                    break;
                }
            }
        }
        return sb.toString();
    }

    public void close() throws IOException {
        is.close();
    }
}
