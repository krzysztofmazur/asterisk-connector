import pl.ychu.asterisk.ami.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Krzysztof on 2014-11-16.
 */
public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException {
        final Configuration conf = new Configuration("192.168.24.4", 5038, "admin", "holi!holi9");
        final Connection connection = new Connection(conf);
        final AsynchronizedConnection conn = new AsynchronizedConnection(connection, new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                System.out.println(System.currentTimeMillis() + ": " + event.getEventName());
            }

            @Override
            public void handleResponse(Response response) {
                System.out.println(response.getMessage());
            }
        });
        conn.start();
        /*
        conn.sendAction(new ListCommands());
        SynchronizedActionSender sender = new SynchronizedActionSender(connection);
        System.out.println(sender.send(new ListCommands()).getMessage());
        sender.closeConnection();*/
    }
}
