package IoTFeds.intracomtelecom.IoTFedsAPI.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SymbioteApiPlatformInfoActuator {
    private PlatformCredentials platformCredentials;

    private String body;

    @ApiModelProperty(notes = "The Id of the platform for which the search request is made.", example = "icom-platform", required = true)
    private String remotePlatformId;

}
