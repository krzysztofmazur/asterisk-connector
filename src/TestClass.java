import pl.ychu.asterisk.manager.*;
import pl.ychu.asterisk.manager.action.Action;
import pl.ychu.asterisk.manager.action.ActionIdGeneratorImpl;
import pl.ychu.asterisk.manager.action.ResponseParserImpl;
import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.event.*;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException, NotConnectedException {
        Connection connection = new Connection();
        MessageHandlerImpl messageHandler = new MessageHandlerImpl();
        messageHandler.setActionIdGenerator(new ActionIdGeneratorImpl());
        connection.setMessageHandler(messageHandler);
        EventProcessor<Event> eventProcessor = new EventProcessor<>();
        eventProcessor.setPattern(Pattern.compile("^.*"));
        eventProcessor.setParser(new StandardEventParser());
        eventProcessor.setHandler(event -> System.out.println(event.getEventName()));
        messageHandler.setDefaultResponseHandler(response1 -> System.out.println(response1.getResponseStatus()));
        messageHandler.addEventProcessor(eventProcessor);
        messageHandler.setResponseParser(new ResponseParserImpl());
        Action loginAction = new Action("Login");
        loginAction.putVariable("username", "admin");
        loginAction.putVariable("secret", "secret");
        connection.connect(loginAction);
        messageHandler.sendAction(new Action("QueueStatus"), response -> System.out.println(response.getResponseStatus()));
        (new Thread(new PingThread(messageHandler))).start();
    }

    private static class PingThread implements Runnable {

        private MessageHandlerImpl messageHandler;

        public PingThread(MessageHandlerImpl messageHandler) {
            this.messageHandler = messageHandler;
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
                    messageHandler.sendAction(new Action("Ping"));
                } catch (IOException | NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
