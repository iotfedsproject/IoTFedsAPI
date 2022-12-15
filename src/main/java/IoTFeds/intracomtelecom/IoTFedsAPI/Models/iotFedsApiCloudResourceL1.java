package IoTFeds.intracomtelecom.IoTFedsAPI.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class iotFedsApiCloudResourceL1 {

    @JsonProperty("credentials")
    private PlatformCredentials platformCredentials;

    @JsonProperty("cloudResource")
    private CloudResourceL1 cloudResourceL1;

}
