package pl.ychu.asterisk;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class AsteriskConfiguration {
    private String hostName;
    private String userName;
    private String userPassword;
    private int hostPort;

    protected AsteriskConfiguration() {
    }

    public AsteriskConfiguration(String hostName, int hostPort, String userName, String userPassword) {
        this.hostName = hostName;
        this.userName = userName;
        this.userPassword = userPassword;
        this.hostPort = hostPort;
    }

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

    @Override
    public String toString() {
        return "AsteriskConfiguration{" +
                "hostName='" + hostName + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", hostPort=" + hostPort +
                '}';
    }
}
