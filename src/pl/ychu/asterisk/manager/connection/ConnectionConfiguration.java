package pl.ychu.asterisk.manager.connection;

public class ConnectionConfiguration {

    private String hostName = "127.0.0.1";

    private String userName = "admin";

    private String userPassword = "secret";

    private int hostPort = 5038;

    private boolean listeningEvents = true;

    private int readTimeout = 30000;

    private int connectTimeout = 5000;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public void setListeningEvents(boolean listeningEvents) {
        this.listeningEvents = listeningEvents;
    }

    public boolean isListeningEvents() {
        return listeningEvents;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
