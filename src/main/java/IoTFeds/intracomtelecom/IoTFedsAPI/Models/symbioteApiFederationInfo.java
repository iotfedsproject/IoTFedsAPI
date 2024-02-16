package IoTFeds.intracomtelecom.IoTFedsAPI.models;

public class symbioteApiFederationInfo {
    private PlatformCredentials platformCredentials;
    private String platformId;
    private String federationId;

    public String getFederationId() {
        return federationId;
    }

    public void setFederationId(String federationId) {
        this.federationId = federationId;
    }

    public PlatformCredentials getPlatformCredentials() {
        return platformCredentials;
    }

    public void setPlatformCredentials(PlatformCredentials platformCredentials) {
        this.platformCredentials = platformCredentials;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}
