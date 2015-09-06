import pl.ychu.asterisk.manager.*;
import pl.ychu.asterisk.manager.EventHandler;
import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.ConnectionConfiguration;
import pl.ychu.asterisk.manager.connection.ConnectionFacade;
import pl.ychu.asterisk.manager.event.*;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException, NotConnectedException {
        ConnectionConfiguration configuration = new ConnectionConfiguration();
        Connection connection = new Connection();
        ConnectionFacade conn = new ConnectionFacade(connection, configuration);
        MessageProcessorImpl messageProcessor = new MessageProcessorImpl();
        EventProcessor eventProcessor = new EventProcessor();
        eventProcessor.setParser(new StandardEventParser());
        eventProcessor.setHandler(new pl.ychu.asterisk.manager.event.EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                System.out.println(event.getEventName());
            }
        });
        messageProcessor.setDefaultResponseHandler(response1 -> {
            System.out.println(response1.getMessage());
        });
        messageProcessor.addEventProcessor(eventProcessor);
        conn.setMessageProcessor(messageProcessor);
        conn.start();
        conn.sendAction(new Action("QueueStatus"), response -> {
            System.out.println(response.getMessage());
        });
    }
}
