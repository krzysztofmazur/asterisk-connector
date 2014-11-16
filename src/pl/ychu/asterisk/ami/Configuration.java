package pl.ychu.asterisk.ami;

/**
 * Created by Krzysztof on 2014-11-10.
 */
public class Configuration {
    private String hostName;
    private String userName;
    private String userPassword;
    private int hostPort;
    private boolean runMaintainingThread = false;

    protected Configuration() {
    }

    /**
     * Konstruktor obiektu służącego jako konfiguracja dla połączeń z asteriskiem.
     *
     * @param hostName
     * @param hostPort
     * @param userName
     * @param userPassword
     */
    public Configuration(String hostName, int hostPort, String userName, String userPassword) {
        this.hostName = hostName;
        this.userName = userName;
        this.userPassword = userPassword;
        this.hostPort = hostPort;
    }

    /**
     * Metoda zwraca informację czy jest w konfiguracji włączony wątek służący do podtrzymywania połączenia.
     *
     * @return true jeśli wątek jest włączony, false jeśli nie jest
     */
    public boolean isEnabledMaintainingThread() {
        return runMaintainingThread;
    }

    /**
     * Metoda pozwala na ustawienie parametru decydującego za włączenie wątka do podrzymywania połączenia.
     *
     * @param runMaintainingThread True jeśli wątek ma być włączony, false jeśli nie.
     */
    public void enableRunMaintainingThread(boolean runMaintainingThread) {
        this.runMaintainingThread = runMaintainingThread;
    }

    /**
     * @return Nazwa hosta.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Metoda pozwalająca na ustawienie nazwy hosta.
     *
     * @param hostName Nazwa hosta.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return Nazwa użytkownika.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Metoda pozwalająca na ustawienie nazwy użytkownika.
     *
     * @param userName Nazwa użytkownika.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return Hasło użytkownika
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Metoda pozwalająca na ustawienie hasła użytkownika.
     *
     * @param userPassword Hasło użytkownia
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * @return Port interfejsu AMI.
     */
    public int getHostPort() {
        return hostPort;
    }

    /**
     * Metoda pozwalająca na ustawienie portu interfejsu AMI.
     *
     * @param hostPort Port interfejsu AMI.
     */
    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "hostName='" + hostName + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", hostPort=" + hostPort +
                '}';
    }
}
