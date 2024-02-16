package IoTFeds.intracomtelecom.IoTFedsAPI.payload;

public class User {


    private String username;

    private String password;

    private String localPlatformId;

    private String clientId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocalPlatformId() {
        return localPlatformId;
    }

    public void setLocalPlatformId(String localPlatformId) {
        this.localPlatformId = localPlatformId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
