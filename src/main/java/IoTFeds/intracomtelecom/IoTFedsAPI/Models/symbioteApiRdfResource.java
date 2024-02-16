package IoTFeds.intracomtelecom.IoTFedsAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.h2020.symbiote.core.internal.RDFInfo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class symbioteApiRdfResource {
    @JsonProperty("credentials")
    private PlatformCredentials platformCredentials;

    @JsonProperty("cloudResource")
    private RDFResource rdfResource;

    @JsonProperty("rdfInfo")
    //The RDFInfo.Rdf should be UrlEncoded
    private RDFInfo rdfInfo;
}
