package IoTFeds.intracomtelecom.IoTFedsAPI.models.baas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckAccessBaas {
    @JsonProperty("product_id")
    public String productId;

    @JsonProperty("user_id")
    public String userid;

    @JsonProperty("data_from")
    public String dataFrom;

    @JsonProperty("data_until")
    public String dataUntil;

    @JsonProperty("frequency")
    public String frequency;

    @JsonProperty("req_observations")
    public String reqObservations;

}