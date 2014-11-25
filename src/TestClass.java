import pl.ychu.asterisk.ami.*;
import pl.ychu.asterisk.ami.action.AOCMessage;
import pl.ychu.asterisk.ami.action.ListCommands;
import pl.ychu.asterisk.ami.exception.NotAuthorizedException;
import pl.ychu.asterisk.ami.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Krzysztof on 2014-11-16.
 */
public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException, NotConnectedException {
        Configuration conf = new Configuration("192.168.24.4", 5038, "admin", "holi!holi9");
        Connection connection = new Connection(conf);
        SynchronizedConnection conn = new SynchronizedConnection(connection, new SynchronizedMessageProcessor(), new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                System.out.println(System.currentTimeMillis() + ": " + event.getEventName());
            }

            @Override
            public void handleResponse(Response response) {
                System.out.println(response.getMessage());
            }
        });
        conn.run();

/*
        conn.sendAction(new ListCommands());
        SynchronizedActionSender sender = new SynchronizedActionSender(connection);
        System.out.println(sender.send(new ListCommands()).getMessage());
        sender.closeConnection();*/
    }
}
