import pl.ychu.asterisk.manager.*;
import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.ConnectionConfiguration;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException, NotConnectedException {
        Connection connection = new Connection(new ConnectionConfiguration());
        AsyncConnector conn = new AsyncConnector(connection, new MessageProcessorImpl());
        conn.addHandler(new EventHandler() {
            @Override
            public void handleEvent(Event event) {
                System.out.println(event.getMessage());
            }

            @Override
            public void handleResponse(Response response) {
            }
        });
        conn.start();
        conn.sendAction(new Action("QueueStatus"), new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                System.out.println(response.getMessage());
            }
        });
    }
}
