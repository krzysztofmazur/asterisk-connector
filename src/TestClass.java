import pl.ychu.asterisk.manager.*;
import pl.ychu.asterisk.manager.action.Action;
import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.connection.ConnectionConfiguration;
import pl.ychu.asterisk.manager.connection.ConnectionFacade;
import pl.ychu.asterisk.manager.event.*;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException, NotConnectedException {
        ConnectionConfiguration configuration = new ConnectionConfiguration();
        Connection connection = new Connection();
        ConnectionFacade conn = new ConnectionFacade(connection, configuration);
        MessageProcessorImpl messageProcessor = new MessageProcessorImpl();
        EventProcessor<Event> eventProcessor = new EventProcessor<>();
        eventProcessor.setPattern(Pattern.compile("^.*"));
        eventProcessor.setParser(new StandardEventParser());
        eventProcessor.setHandler(event -> System.out.println(event.getEventName()));
        messageProcessor.setDefaultResponseHandler(response1 -> System.out.println(response1.getMessage()));
        messageProcessor.addEventProcessor(eventProcessor);
        conn.setMessageProcessor(messageProcessor);
        conn.start();
        conn.sendAction(new Action("QueueStatus"), response -> System.out.println(response.getMessage()));
        (new Thread(new PingThread(conn))).start();
    }

    private static class PingThread implements Runnable {

        private ConnectionFacade connectionFacade;

        public PingThread(ConnectionFacade connectionFacade) {
            this.connectionFacade = connectionFacade;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    break;
                }
                try {
                    connectionFacade.sendAction(new Action("Ping"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
