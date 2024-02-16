package IoTFeds.intracomtelecom.IoTFedsAPI.utilities.product;

import IoTFeds.intracomtelecom.IoTFedsAPI.models.product.AccessToken;
import IoTFeds.intracomtelecom.IoTFedsAPI.models.product.ObservationParameters;
import IoTFeds.intracomtelecom.IoTFedsAPI.models.product.SymbioteApiProduct;
import IoTFeds.intracomtelecom.IoTFedsAPI.services.baas.BaasClient;
import IoTFeds.intracomtelecom.IoTFedsAPI.utilities.resource.ResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory;
import eu.h2020.symbiote.client.interfaces.CRAMClient;
import eu.h2020.symbiote.client.interfaces.RAPClient;
import eu.h2020.symbiote.client.interfaces.RHClient;
import eu.h2020.symbiote.cloud.model.internal.CloudResource;
import eu.h2020.symbiote.core.internal.cram.ResourceUrlsResponse;
import eu.h2020.symbiote.model.cim.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ProductService {

    private Boolean baasIntegration;
    private String getGlobalProducts;
    private String getFederatedProducts;
    private String getFederatedResources;
    private String getUserTokes;
    private String getReceipt;
    private String getAllResourcesInfo;
    private String getAllProductsInfo;
    private String getResourceInfo;
    private String getProductInfo;
    private String checkAccess;

    private String decreaseAccess;
    private String createProduct;

    private String baasBaseUrl;
    private int connectTimeoutSeconds;

    private BaasClient baasClient;

    private AbstractSymbIoTeClientFactory factory = null;

    private static Log log = LogFactory.getLog(ResourceService.class);

    private Properties properties = new Properties();

    public ProductService(AbstractSymbIoTeClientFactory clientFactory) {

        factory = clientFactory;
        try {
            InputStream inputStream = ResourceService.class
                    .getClassLoader()
                    .getResourceAsStream("baas.properties");
            properties.load(inputStream);
            inputStream.close();
            baasIntegration = Boolean.parseBoolean(properties.getProperty("baas.integration"));
            connectTimeoutSeconds = Integer.parseInt(properties.getProperty("baas.connectTimeoutSeconds"));
            baasBaseUrl = properties.getProperty("baas.baseUrl");
            getGlobalProducts = properties.getProperty("baas.getGlobalProducts.url");
            getFederatedProducts = properties.getProperty("baas.getFederatedProducts.url");
            getFederatedResources = properties.getProperty("baas.getFederatedResources.url");
            getUserTokes = properties.getProperty("baas.getUserTokes.url");
            getReceipt = properties.getProperty("baas.getReceipt.url");
            getAllResourcesInfo = properties.getProperty("baas.getAllResourcesInfo.url");
            getAllProductsInfo = properties.getProperty("baas.getAllProductsInfo.url");
            getResourceInfo = properties.getProperty("baas.getResourceInfo.url");
            getProductInfo = properties.getProperty("baas.getProductInfo.url");
            checkAccess = properties.getProperty("baas.checkAccess.url");
            createProduct = properties.getProperty("baas.createProduct.url");
            decreaseAccess = properties.getProperty("baas.decrease_access.url");
            RestTemplateBuilder builder = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds));
            baasClient = new BaasClient(builder);
        } catch (Exception e) {
            // process the exception
        }
    }


    public ResponseEntity<?> checkAccessBaas(String productId, String userId, String dateFrom, String dateTo, String frequency, String reqObservations) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        HashMap<String, String> body = new HashMap<>();

        body.put("product_id", productId);
        body.put("user_id", userId);
        body.put("data_from", dateFrom);
        body.put("data_until", dateTo);
        body.put("frequency", frequency);
        body.put("req_observations", reqObservations);

        return baasClient.makeBaasHttpRequest(baasBaseUrl, checkAccess, HttpMethod.POST, body, parameters);
    }

    public ResponseEntity<String> redeemAccessTokenUsage(String productId, String userId, Integer timesUsed) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        HashMap<String, String> body = new HashMap<>();

        body.put("product_id", productId);
        body.put("user_id", userId);
        body.put("times_used", String.valueOf(timesUsed));

        return baasClient.makeBaasHttpRequest(baasBaseUrl, decreaseAccess, HttpMethod.POST, body, parameters);
    }

    public ResponseEntity<String> getUserTokensBaas(String userId) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("user_id", userId);

        HashMap<String, String> body = new HashMap<>();


        return baasClient.makeBaasHttpRequest(baasBaseUrl, checkAccess, HttpMethod.GET, body, parameters);
    }

    public ResponseEntity<?> accessProduct(SymbioteApiProduct symbioteApiProduct) {
        String productId = symbioteApiProduct.getProduct().getId();
        String username = symbioteApiProduct.getPlatformCredentials().getUsername();
        String dateFrom = symbioteApiProduct.getDateFrom();
        String dateTo = symbioteApiProduct.getDateTo();
        String reqObservations = symbioteApiProduct.getReqObservations();

        CRAMClient cramClient = factory.getCramClient();
        RAPClient rapClient = factory.getRapClient();
        ObjectMapper mapper = new ObjectMapper();

        if (baasIntegration) {
            ResponseEntity<?> baasResponse = checkAccessBaas(productId, username, dateFrom, dateTo, "", reqObservations);
            if (baasResponse.getStatusCode() != HttpStatus.OK) {
                ResponseEntity<String> userTokensResponse = getUserTokensBaas(username);
                if (userTokensResponse.getStatusCode() != HttpStatus.OK) {
                    try {
                        Map<String, AccessToken> userTokensList = mapper.readValue(userTokensResponse.getBody(), new TypeReference<Map<String, AccessToken>>() {});
                        return new ResponseEntity<>("The max observations that you can have are " + userTokensList.get(productId).getAccessTimes(), HttpStatus.BAD_REQUEST);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Map<String, List<Observation>> observationMap = getStringListMap(symbioteApiProduct, cramClient, rapClient, Integer.parseInt(reqObservations));

        Set<Integer> resources = observationMap.values().stream()
                .map(List::size)
                .collect(Collectors.toSet());

        if (resources.isEmpty()){
            return new ResponseEntity<>("No observations found", HttpStatus.BAD_REQUEST);
        }
        if (resources.stream().anyMatch(value -> value == 0)) {
            return new ResponseEntity<>("A resource has 0 observations", HttpStatus.BAD_REQUEST);
        }

        while (resources.size() >= 2) {
            int minimumValue = resources.stream().mapToInt(Integer::intValue).min().orElse(0);
            observationMap = getStringListMap(symbioteApiProduct, cramClient, rapClient, minimumValue);
            resources = observationMap.values().stream()
                    .map(List::size)
                    .collect(Collectors.toSet());
        }

        redeemAccessTokenUsage(
                productId,
                username,
                resources.stream().mapToInt(Integer::intValue).min().orElse(0));
        return new ResponseEntity<>(observationMap, HttpStatus.OK);
    }

    private Map<String, List<Observation>> getStringListMap(SymbioteApiProduct symbioteApiProduct, CRAMClient cramClient, RAPClient rapClient, Integer topObservations) {
        String resourceId;
        String resourceInternalId;
        String platformId;
        Map<String, List<Observation>> observationMap  = new HashMap<>();
        for (ObservationParameters observationParameters : symbioteApiProduct.getProduct().getObservationParameters()) {

            resourceInternalId = observationParameters.getResourceId();
            platformId = observationParameters.getPlatformId();
            resourceId = getResourceIdFromInternalId(platformId, resourceInternalId);

            Set<String> platformIds = new HashSet<>(Collections.singletonList(platformId));
            ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
            String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);

            List<Observation> observedProduct = rapClient.getObservationsByQuery(
                    resourceUrl,
                    topObservations,
                    symbioteApiProduct.getDateFrom(),
                    symbioteApiProduct.getDateTo(),
                    false,
                    platformIds
            );
            observationMap.put(resourceId, observedProduct);
        }
        return observationMap;
    }

    private String getResourceIdFromInternalId(String platformId, String resourceInternalId) {
        String resourceId = null;
        RHClient rhClient = null;
        CloudResource cloudResource = null;
        try {
            rhClient = factory.getRHClient(platformId);
            cloudResource = rhClient.getResource(resourceInternalId);
            resourceId = cloudResource.getResource().getId();
        } catch (Exception e) {
            if (rhClient == null) {
            }
            if (cloudResource == null) {
            }
        }

        return resourceId;
    }
}
