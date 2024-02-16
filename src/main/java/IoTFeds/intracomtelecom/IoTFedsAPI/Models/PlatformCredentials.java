package IoTFeds.intracomtelecom.IoTFedsAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class PlatformCredentials {
    @JsonProperty("platformId")
    @ApiModelProperty(notes = "Platform from which the user requests a token", example = "icom-platform", required = true)
    private String localPlatformId;
    @JsonProperty("username")
    @ApiModelProperty(notes = "Platform user", example = "username", required = true)
    private String username;
    @JsonProperty("password")
    @ApiModelProperty(notes = "Platform user password", example = "password", required = true)
    private String password;
    @JsonProperty("clientId")
    @ApiModelProperty(notes = "Client Id that will be used for the requests", example = "Test_Client", required = false)
    private String clientId;

    public String getLocalPlatformId() {
        return localPlatformId;
    }

    public void setLocalPlatformId(String localPlatformId) {
        this.localPlatformId = localPlatformId;
    }

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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
