package IoTFeds.intracomtelecom.IoTFedsAPI.models.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

    @JsonProperty("AccessTimes")
    private String accessTimes;
    @JsonProperty("DataAvailableFrom")
    private String dataAvailableFrom;
    @JsonProperty("DataAvailableUntil")
    private String dataAvailableUntil;
    @JsonProperty("Marketplace")
    private String marketplace;
    @JsonProperty("ValidUntil")
    private String validUntil;
    @JsonProperty("toBeExchanged")
    private Boolean toBeExchanged;
    @JsonProperty("userHasRated")
    private Boolean userHasRated;

}