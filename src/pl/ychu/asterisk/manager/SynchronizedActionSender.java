package pl.ychu.asterisk.manager;

import pl.ychu.asterisk.manager.connection.Connection;
import pl.ychu.asterisk.manager.exception.NotAuthorizedException;
import pl.ychu.asterisk.manager.exception.NotConnectedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SynchronizedActionSender {
    private AsynchronizedConnection connector;

    public SynchronizedActionSender(Connection connection) throws IOException, NotAuthorizedException {
        this.connector = new AsynchronizedConnection(connection, new SynchronizedMessageProcessor());
        this.connector.enableMaintainingThread(true);
        this.connector.start();
    }

    public void closeConnection() throws NotConnectedException {
        this.connector.stop();
    }

    public Response send(Action action) throws InterruptedException, IOException, TimeoutException, NotConnectedException {
        return this.send(action, 30000);
    }

    public Response send(Action action, int timeout) throws InterruptedException, TimeoutException, IOException, NotConnectedException {
        final ActionSyncSenderHelper helper = new ActionSyncSenderHelper();
        connector.sendAction(action, helper::setResponse);
        long startTime = System.currentTimeMillis() + timeout;
        while (startTime > System.currentTimeMillis()) {
            if (helper.hasResponse()) {
                return helper.getResponse();
            }
            Thread.sleep(1);
        }
        throw new TimeoutException();
    }

    private class ActionSyncSenderHelper {
        private Response response;

        public ActionSyncSenderHelper() {
        }

        public Response getResponse() {
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }

        public boolean hasResponse() {
            return response != null;
        }
    }
}
