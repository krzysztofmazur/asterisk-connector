package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

/**
 * Created by Krzysztof on 2014-11-13.
 */
public class Monitor extends Action {
    private String channel;
    private String file;
    private String format;
    private String mix;

    public Monitor(String channel, String file, String format, String mix) {
        this.channel = channel;
        this.file = file;
        this.format = format;
        this.mix = mix;
    }

    @Override
    protected String getMessage() {
        return "Action: Monitor\n"
                + "Channel: " + channel + "\n"
                + (file != null ? "File: " + file + "\n" : "")
                + (format != null ? "Format: " + format + "\n" : "")
                + (mix != null ? "Mix: " + mix + "\n" : "");
    }
}
