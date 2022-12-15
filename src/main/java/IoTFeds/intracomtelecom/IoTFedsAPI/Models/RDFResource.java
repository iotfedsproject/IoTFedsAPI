package IoTFeds.intracomtelecom.IoTFedsAPI.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.h2020.symbiote.model.cim.Resource;
import eu.h2020.symbiote.security.accesspolicies.common.IAccessPolicySpecifier;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RDFResource {
    @Id
    @JsonProperty("internalId")
    private String internalId;
    @JsonProperty("pluginId")
    private String pluginId;

    @JsonProperty("accessPolicy")
    private IAccessPolicySpecifier accessPolicy;
    @JsonProperty("filteringPolicy")
    private IAccessPolicySpecifier filteringPolicy;

    @NotNull
    @JsonProperty("idMapKey")
    private String idMapKey;
}
