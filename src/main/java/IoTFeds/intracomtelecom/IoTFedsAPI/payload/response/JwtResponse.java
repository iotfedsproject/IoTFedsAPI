package IoTFeds.intracomtelecom.IoTFedsAPI.payload.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private String username;
  private String localPlatformId;
  private String clientId;
  private List<String> roles;

  public JwtResponse(String accessToken, String username, String localPlatformId, String clientId, List<String> roles) {
    this.token = accessToken;
    this.username = username;
    this.localPlatformId = localPlatformId;
    this.clientId = clientId;
    this.roles = roles;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }
}
