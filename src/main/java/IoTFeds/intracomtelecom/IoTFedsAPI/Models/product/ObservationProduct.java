package IoTFeds.intracomtelecom.IoTFedsAPI.models.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObservationProduct extends Product {
    private String dateTimeFrom;
    private String dateTimeTo;
    private int access;
}