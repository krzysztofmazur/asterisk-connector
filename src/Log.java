import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public static void a() {
        notify("a");
    }

    private static void notify(String letter) {
        System.out.println(String.format("%s %s", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date()), letter));
    }
}
