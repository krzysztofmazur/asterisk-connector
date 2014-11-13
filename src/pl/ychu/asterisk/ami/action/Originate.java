package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Originate extends Action {
    private String channel;
    private String exten;
    private String context;
    private int priority;
    private String application;
    private String data;
    private int timeout;
    private String callerId;
    private String variable;
    private String account;
    private String async;
    private String codecs;

    public Originate(String channel, String exten, String context, int priority, String application, String data, int timeout, String callerId, String variable, String account, String async, String codecs) {
        this.channel = channel;
        this.exten = exten;
        this.context = context;
        this.priority = priority;
        this.application = application;
        this.data = data;
        this.timeout = timeout;
        this.callerId = callerId;
        this.variable = variable;
        this.account = account;
        this.async = async;
        this.codecs = codecs;
    }


    @Override
    protected String getMessage() {
        return "Action: Originate\n"
                + "Channel: " + channel + "\n"
                + (exten != null ? "Mix: " + exten + "\n" : "")
                + (context != null ? "Context: " + context + "\n" : "")
                + (priority != -1 ? "Priority: " + priority + "\n" : "")
                + (application != null ? "Application: " + application + "\n" : "")
                + (data != null ? "Data: " + data + "\n" : "")
                + (timeout != -1 ? "Timeout: " + timeout + "\n" : "")
                + (callerId != null ? "CallerID: " + callerId + "\n" : "")
                + (variable != null ? "Variable: " + variable + "\n" : "")
                + (account != null ? "Account: " + account + "\n" : "")
                + (async != null ? "Async: " + async + "\n" : "")
                + (codecs != null ? "Codecs: " + codecs + "\n" : "");
    }
}
