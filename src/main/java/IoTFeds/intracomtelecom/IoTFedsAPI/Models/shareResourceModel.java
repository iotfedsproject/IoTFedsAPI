package IoTFeds.intracomtelecom.IoTFedsAPI.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class shareResourceModel {
    private PlatformCredentials platformCredentials;
    private String resourceInternalId;
    private boolean bartered;
    private String federationId;
}
