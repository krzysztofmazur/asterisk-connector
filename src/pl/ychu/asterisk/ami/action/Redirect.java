package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-14.
 */
public class Redirect extends Action {
    private String channel;
    private String extraChannel;
    private String exten;
    private String extraExten;
    private String context;
    private String extraContext;
    private String priority;
    private String extraPriority;

    public Redirect(String channel, String exten, String context, String priority) {
        this(channel, null, exten, null, context, null, priority, null);
    }

    public Redirect(String channel, String extraChannel, String exten, String extraExten, String context, String extraContext, String priority, String extraPriority) {
        this.channel = channel;
        this.extraChannel = extraChannel;
        this.exten = exten;
        this.extraExten = extraExten;
        this.context = context;
        this.extraContext = extraContext;
        this.priority = priority;
        this.extraPriority = extraPriority;
    }

    @Override
    protected String getMessage() {
        return "Action: Redirect\n"
                + "Channel: " + channel + "\n"
                + (extraChannel != null ? "ExtraChannel: " + extraChannel + "\n" : "")
                + "Exten: " + exten + "\n"
                + (extraExten != null ? "ExtraExten: " + extraExten + "\n" : "")
                + "Context: " + context + "\n"
                + (extraContext != null ? "ExtraContext: " + extraContext + "\n" : "")
                + "Priority: " + priority + "\n"
                + (extraPriority != null ? "ExtraPriority: " + extraPriority + "\n" : "");
    }
}
