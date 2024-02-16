package IoTFeds.intracomtelecom.IoTFedsAPI.models.baas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBaas {
    @JsonProperty("FedMarketplace_id")
    public String fedMarketplace_id;
    @JsonProperty("GlobalMarketplace_id")
    public boolean globalMarketplace_id;
    @JsonProperty("Product_details")
    public Map<String, String> product_details;
    @JsonProperty("Product_id")
    public String product_id;
    @JsonProperty("Reputation")
    public int reputation;
    @JsonProperty("Resource_ids")
    public ResourcesIdsBaas resource_ids;
    @JsonProperty("Seller")
    public String seller;
    public String docType;
    public ArrayList<Object> subjReputation;
    public int transactionCounter;
}