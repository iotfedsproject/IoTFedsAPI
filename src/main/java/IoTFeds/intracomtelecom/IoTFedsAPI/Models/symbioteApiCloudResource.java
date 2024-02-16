package IoTFeds.intracomtelecom.IoTFedsAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.h2020.symbiote.cloud.model.internal.CloudResource;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class symbioteApiCloudResource {

    @JsonProperty("credentials")
    private PlatformCredentials platformCredentials;

    @JsonProperty("cloudResource")
    private CloudResource cloudResource;

}
