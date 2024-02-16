package IoTFeds.intracomtelecom.IoTFedsAPI.models.product;

import eu.h2020.symbiote.model.cim.Observation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductObservation {
    private String resourceId;

    private String platformId;

    private Observation observation;
}


//            String resourceId = observationParameters.get(i).resourceId;
//            String platformId = observationParameters.get(i).platformId;
//            observationArray.put(i,ProductUtils.getObservation(resourceId,platformId));