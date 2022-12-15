package IoTFeds.intracomtelecom.IoTFedsAPI.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.querydsl.core.annotations.QueryEntity;
import eu.h2020.symbiote.model.cim.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Actuator.class, name = "Actuator"),
        @JsonSubTypes.Type(value = Service.class, name = "Service"),
        @JsonSubTypes.Type(value = Device.class, name = "Device"),
        @JsonSubTypes.Type(value = StationarySensor.class, name = "StationarySensor"),
        @JsonSubTypes.Type(value = MobileSensor.class, name = "MobileSensor")
})
@ApiModel(description = "Description of a Resource. " +
        "Can be one of following subclasses: Actuator, Service, ActuatingService, StationarySensor, StationaryDevice, MobileSensor, MobileDevice " +
        "(consult SymbIoTeLibraries documentation for API).")
@QueryEntity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceL1 {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private List<String> description;

    @JsonProperty("interworkingServiceURL")
    private String interworkingServiceURL;

}
