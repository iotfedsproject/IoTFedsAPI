package IoTFeds.intracomtelecom.IoTFedsAPI.models;

import io.swagger.annotations.ApiModelProperty;

public class symbioteApiPlatformInfo {
    private PlatformCredentials platformCredentials;

    public PlatformCredentials getPlatformCredentials() {
        return platformCredentials;
    }

    public void setPlatformCredentials(PlatformCredentials platformCredentials) {
        this.platformCredentials = platformCredentials;
    }

    public String getRemotePlatformId() {
        return remotePlatformId;
    }

    public void setRemotePlatformId(String remotePlatformId) {
        this.remotePlatformId = remotePlatformId;
    }

    @ApiModelProperty(notes = "The Id of the platform for which the search request is made.", example = "icom-platform", required = true)
    private String remotePlatformId;
}
