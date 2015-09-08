import pl.ychu.asterisk.manager.connection.MessageListener;
import pl.ychu.asterisk.manager.standard.action.Action;
import pl.ychu.asterisk.manager.standard.action.ActionIdGenerator;
import pl.ychu.asterisk.manager.standard.action.ResponseParserImpl;
import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.standard.StandardMessageHandler;
import pl.ychu.asterisk.manager.standard.event.*;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class TestClass {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, NotAuthorizedException, NotConnectedException {
        StandardMessageHandler messageHandler = new StandardMessageHandler();
        messageHandler.setActionIdGenerator(new ActionIdGenerator());
        //messageHandler.setDefaultResponseListener(response1 -> System.out.println(response1.getResponseStatus()));
        messageHandler.setResponseParser(new ResponseParserImpl());
        /*EventProcessor<Event> eventProcessor = new EventProcessor<>();
        eventProcessor.setPattern(Pattern.compile("^.*"));
        eventProcessor.setParser(new StandardEventParser());
        eventProcessor.setListener(event -> System.out.println(event.getEventName()));
        messageHandler.addEventProcessor(eventProcessor);*/
        Action loginAction = new Action("Login");
        loginAction.putVariable("username", "admin");
        loginAction.putVariable("secret", "secret");

        Connection connection = new Connection();
        connection.registerMessageHandler(messageHandler);
        connection.registerMessageListener(new MessageListener() {
            @Override
            public void onIncomingMessage(String message) {
                System.out.println(message);
            }

            @Override
            public void onOutgoingMessage(String message) {
                System.out.println(message);
            }
        });
        messageHandler.connect(loginAction);
        //messageHandler.sendAction(new Action("QueueStatus"), response -> System.out.println(response.getResponseStatus()));
        (new Thread(new PingThread(messageHandler))).start();
    }

    private static class PingThread implements Runnable {

        private StandardMessageHandler messageHandler;

        public PingThread(StandardMessageHandler messageHandler) {
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
