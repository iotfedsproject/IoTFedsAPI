package IoTFeds.intracomtelecom.IoTFedsAPI.models.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "streaming", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StreamingProduct.class, name = "true"),
        @JsonSubTypes.Type(value = ObservationProduct.class, name = "false")
})
public class Product {
    @Id
    private String id;
    private String sellerId;
    private double price;
    private String fedMarketplaceId;
    private boolean globalMarketplaceId;
    private String name;
    private List<String> description;
    private Map<String, Object> productDetails;
    private List<String> observedProperties;
    private List<ObservationParameters> observationParameters;
    private List<String> vertical;
    private boolean streaming;


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        Product product = (Product) o;
//        return com.google.common.base.Objects.equal(id, product.id);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}






