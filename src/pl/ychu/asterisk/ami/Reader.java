package pl.ychu.asterisk.ami;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Krzysztof on 2014-11-11.
 */
public class Reader {

    private InputStream is;
    private StringBuilder sb;

    protected Reader() {
    }

    public Reader(InputStream is) {
        this.is = is;
    }

    public String readMessage() throws IOException {
        sb = new StringBuilder(200);
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
