package pl.ychu.asterisk.ami;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by krzysztof on 15.11.14.
 */
public class SynchronizedActionSender {
    private Connector connector;

    public SynchronizedActionSender(Connector connector) {
        this.connector = connector;
    }

    public Response send(Action action) throws InterruptedException, IOException, TimeoutException {
        return this.send(action, 30000);
    }

    public Response send(Action action, int timeout) throws InterruptedException, TimeoutException, IOException {
        final ActionSyncSenderHelper helper = new ActionSyncSenderHelper();
        connector.sendAction(action, new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                helper.setResponse(response);
            }
        });
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
