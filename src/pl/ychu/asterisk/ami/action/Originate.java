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
                + (context != null ? "Mix: " + context + "\n" : "")
                + (priority != -1 ? "Mix: " + priority + "\n" : "")
                + (application != null ? "Mix: " + application + "\n" : "")
                + (data != null ? "Mix: " + data + "\n" : "")
                + (timeout != -1 ? "Mix: " + timeout + "\n" : "")
                + (callerId != null ? "Mix: " + callerId + "\n" : "")
                + (variable != null ? "Mix: " + variable + "\n" : "")
                + (account != null ? "Mix: " + account + "\n" : "")
                + (async != null ? "Mix: " + async + "\n" : "")
                + (codecs != null ? "Mix: " + codecs + "\n" : "");
    }
}
