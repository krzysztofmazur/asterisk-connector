package pl.ychu.asterisk.ami;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class ActionId {
    private static long actionId = 0;
    private static Object mutex = new Object();

    public static String getNext() {
        synchronized (mutex) {
            ActionId.actionId++;
            return System.currentTimeMillis() + String.format("%09d", ActionId.actionId);
        }
    }
}
