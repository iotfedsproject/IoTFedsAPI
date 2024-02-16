package IoTFeds.intracomtelecom.IoTFedsAPI.models.product;

import eu.h2020.symbiote.model.cim.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ObservationParameters {

    private String resourceId;

    private String platformId;

    private String interWorkingServiceUrl;

    private Location location;

}
