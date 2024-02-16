package IoTFeds.intracomtelecom.IoTFedsAPI.models.product;

import IoTFeds.intracomtelecom.IoTFedsAPI.models.PlatformCredentials;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.h2020.symbiote.cloud.model.internal.CloudResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SymbioteApiProduct {
    @JsonProperty("credentials")
    private PlatformCredentials platformCredentials;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("dateFrom")
    private String dateFrom;

    @JsonProperty("dateTo")
    private String dateTo;

    @JsonProperty("reqObservations")
    private String reqObservations;
}
