package IoTFeds.intracomtelecom.IoTFedsAPI.Models;

public class UserRegistrationDetails {

    private String new_username;
    private String email;
    private String new_password;
    private String aam_owner_username;
    private String aam_owner_password;
    private String platformId;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getAam_owner_username() {
        return aam_owner_username;
    }

    public void setAam_owner_username(String aam_owner_username) {
        this.aam_owner_username = aam_owner_username;
    }

    public String getAam_owner_password() {
        return aam_owner_password;
    }

    public void setAam_owner_password(String aam_owner_password) {
        this.aam_owner_password = aam_owner_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNew_username() {
        return new_username;
    }

    public void setNew_username(String new_username) {
        this.new_username = new_username;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
