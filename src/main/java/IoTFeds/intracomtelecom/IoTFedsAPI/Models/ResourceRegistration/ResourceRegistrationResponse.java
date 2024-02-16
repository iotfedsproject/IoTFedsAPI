package IoTFeds.intracomtelecom.IoTFedsAPI.models.resourceRegistration;

import eu.h2020.symbiote.cloud.model.internal.CloudResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResourceRegistrationResponse {
    List<CloudResource> cloudResources;
    Map<String, String> errors;
}
