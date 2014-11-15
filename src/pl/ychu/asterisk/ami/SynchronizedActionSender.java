package pl.ychu.asterisk.ami;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

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
        final ResponseSyncSenderHelper helper = new ResponseSyncSenderHelper();
        connector.sendAction(action, new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                helper.response = response;
            }
        });
        long startTime = System.currentTimeMillis();
        while (startTime + timeout > System.currentTimeMillis()) {
            if (helper.response != null) {
                return helper.response;
            }
            Thread.sleep(1);
        }
        throw new TimeoutException();
    }

    private class ResponseSyncSenderHelper {
        public Response response;
    }
}
