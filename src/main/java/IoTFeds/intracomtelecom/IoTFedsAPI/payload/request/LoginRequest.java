package IoTFeds.intracomtelecom.IoTFedsAPI.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    @JsonProperty("username")
    @ApiModelProperty(notes = "Platform user", example = "username", required = true)
    private String username;

    @NotBlank
    @JsonProperty("password")
    @ApiModelProperty(notes = "Platform user password", example = "password", required = true)
    private String password;

    @JsonProperty("platformId")
    @ApiModelProperty(notes = "Platform from which the user requests a token", example = "icom-platform", required = true)
    private String localPlatformId;

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
}
