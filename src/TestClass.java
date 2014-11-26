import pl.ychu.asterisk.manager.*;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Krzysztof on 2014-11-16.
 */
public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException, NotConnectedException {
        Configuration conf = new Configuration("192.168.24.4", 5038, "admin", "holi!holi9");
        Connection connection = new Connection(conf);
        AsynchronizedConnection conn = new AsynchronizedConnection(connection, new AsynchronizedMessageProcessor(), new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                //System.out.println(System.currentTimeMillis() + ": " + event.getEventName());
                UnifiedEvent e = (UnifiedEvent) event;
                System.out.println(e.getMessage());
            }

            @Override
            public void handleResponse(Response response) {
                //System.out.println(response.getMessage());
            }
        });
        conn.start();
        conn.sendAction(new UnifiedAction("QueueStatus"), new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                System.out.println(response.getMessage());
            }
        });

/*
        conn.sendAction(new ListCommands());
        SynchronizedActionSender sender = new SynchronizedActionSender(connection);
        System.out.println(sender.send(new ListCommands()).getMessage());
        sender.closeConnection();*/
    }
}
