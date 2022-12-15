package IoTFeds.intracomtelecom.IoTFedsAPI.Utilities.ResourceUtils;

import IoTFeds.intracomtelecom.IoTFedsAPI.Models.PlatformCredentials;
import IoTFeds.intracomtelecom.IoTFedsAPI.Models.ResourceRegistration.ResourceRegistrationResponse;
import IoTFeds.intracomtelecom.IoTFedsAPI.Models.iotFedsApiCloudResourceL1;
import IoTFeds.intracomtelecom.IoTFedsAPI.Models.shareResourceModel;
import IoTFeds.intracomtelecom.IoTFedsAPI.Models.symbioteApiRdfResource;
import IoTFeds.intracomtelecom.IoTFedsAPI.services.Baas.BaasClient;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory;
import eu.h2020.symbiote.client.interfaces.*;
import eu.h2020.symbiote.cloud.model.internal.*;
import eu.h2020.symbiote.core.ci.QueryResourceResult;
import eu.h2020.symbiote.core.ci.QueryResponse;
import eu.h2020.symbiote.core.internal.CoreQueryRequest;
import eu.h2020.symbiote.core.internal.RDFInfo;
import eu.h2020.symbiote.core.internal.cram.ResourceUrlsResponse;
import eu.h2020.symbiote.model.cim.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public class ResourceService {

    private Boolean baasIntegration;
    private String registerResourceToBC;
    private String deleteResourceFromBC;
    private String baasBaseUrl;
    private int connectTimeoutSeconds;

    private BaasClient baasClient;

    private AbstractSymbIoTeClientFactory factory = null;

    private static Log log = LogFactory.getLog(ResourceService.class);

    private Properties properties =  new Properties();

    public ResourceService(AbstractSymbIoTeClientFactory clientFactory) {

        factory = clientFactory;
        try {
            InputStream inputStream = ResourceService.class
                    .getClassLoader()
                    .getResourceAsStream("baas.properties");
            properties.load(inputStream);
            inputStream.close();
            baasIntegration = Boolean.parseBoolean(properties.getProperty("baas.integration"));
            connectTimeoutSeconds = Integer.parseInt(properties.getProperty("baas.connectTimeoutSeconds"));
            registerResourceToBC = properties.getProperty("baas.registerResource.url");
            deleteResourceFromBC = properties.getProperty("baas.deleteResource.url");
            baasBaseUrl = properties.getProperty("baas.baseUrl");
            RestTemplateBuilder builder = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds));
            baasClient = new BaasClient(builder);
        } catch (Exception e) {
            // process the exception
        }
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

    public FederatedResource getL2Resource(String homePlatformId, String resourceInternalId, String federationId) {
        PRClient searchClient = factory.getPRClient(homePlatformId);
        PlatformRegistryQuery registryQuery = new PlatformRegistryQuery.Builder()
                .federationIds(Collections.singletonList(federationId))
                .build();
        // The set of platforms from which we are going to request credentials for our requests
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        FederationSearchResult result = searchClient.search(registryQuery, false, platformIds);
        FederatedResource federatedResource = result.getResources().stream()
                .filter(resource -> resourceInternalId.equals(resource.getCloudResource().getInternalId()))
                .findAny()
                .orElse(null);
        return federatedResource;
    }

    public List<QueryResourceResult> getListL1(String homePlatformId, String platformId) {
        SearchClient searchClient = factory.getSearchClient();
        // The set of platforms from which we are going to request credentials for our requests
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        CoreQueryRequest coreQueryRequest = new CoreQueryRequest.Builder()
                .platformId(platformId)
                .build();
        System.out.println("Searching the Platform Registry of platform: " + platformId);
        QueryResponse queryResponse = searchClient.search(coreQueryRequest, false, platformIds);
        return queryResponse.getResources();
    }

    public List<FederatedResource> getListL2(String homePlatformId, String federationId) {
        PRClient searchClient = factory.getPRClient(homePlatformId);
        // The set of platforms from which we are going to request credentials for our requests
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        PlatformRegistryQuery registryQuery = new PlatformRegistryQuery.Builder()
                .federationIds(Collections.singletonList(federationId))
                .build();
        System.out.println("Searching the Platform Registry of platform: " + homePlatformId);
        FederationSearchResult result = searchClient.search(registryQuery, false, platformIds);
        return result.getResources();
    }

    public Observation accessL1Resource(String homePlatformId, String platformId, String resourceInternalId) {
//        SearchClient searchClient = factory.getSearchClient();
        String resourceId = getResourceIdFromInternalId(platformId, resourceInternalId);
        CRAMClient cramClient = factory.getCramClient();
        RAPClient rapClient = factory.getRapClient();


        /*
        Search for resources in the Platform Registry of the specified platform
         */
        // The set of platforms from which we are going to request credentials for our requests
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
//        // Create the request
//        CoreQueryRequest coreQueryRequest = new CoreQueryRequest.Builder()
//                .platformId(platformId).id(resourceId)
//                .build();
//
//        QueryResponse queryResponse = searchClient.search(coreQueryRequest,false, platformIds);
//        List<QueryResourceResult> result = queryResponse.getResources().stream().collect(Collectors.toList());

//
//        try {
//            System.out.println(om.writeValueAsString(queryResponse));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        if (queryResponse.getResources().size() == 0) {
//            System.out.println("Could not find any resources");
//        }
        ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
        String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);
        Observation observation = rapClient.getLatestObservation(resourceUrl, false, platformIds);

        return observation;
    }

    public void accessL1ResourceActuator(String homePlatformId, String body, String platformId, String resourceInternalId) {
        String resourceId = getResourceIdFromInternalId(platformId, resourceInternalId);
        CRAMClient cramClient = factory.getCramClient();
        RAPClient rapClient = factory.getRapClient();
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
        String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);
        rapClient.actuate(resourceUrl, body, false, platformIds);
    }

    public void accessL1ResourceService(String homePlatformId, String body, String platformId, String resourceInternalId) {
        String resourceId = getResourceIdFromInternalId(platformId, resourceInternalId);
        CRAMClient cramClient = factory.getCramClient();
        RAPClient rapClient = factory.getRapClient();
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
        String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);
        rapClient.invokeService(resourceUrl, body, false, platformIds);
    }

    public Observation accessL1ResourceWithId(String homePlatformId, String platformId, String resourceId) {
        CRAMClient cramClient = factory.getCramClient();
        RAPClient rapClient = factory.getRapClient();
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
        String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);
        Observation observation = rapClient.getLatestObservation(resourceUrl, false, platformIds);

        return observation;
    }

    public Observation accessL2Resource(String homePlatformId, String resourceInternalId, String federationId) {
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        PRClient searchClient = factory.getPRClient(homePlatformId);
        RAPClient rapClient = factory.getRapClient();

        // Create the request
        PlatformRegistryQuery registryQuery = new PlatformRegistryQuery.Builder()
                .federationIds(Collections.singletonList(federationId))
                .build();

        FederationSearchResult result = searchClient.search(registryQuery, false, platformIds);

        FederatedResource federatedResource = result.getResources().stream()
                .findAny()
                .orElse(null);

        FederatedResourceInfo resourceInfo = federatedResource.getFederatedResourceInfoMap().get(federationId);
        String resourceUrl = resourceInfo.getoDataUrl();

        Observation observation = rapClient.getLatestObservation(resourceUrl, false, platformIds);
        return observation;
    }

    public List<QueryResourceResult> search(CoreQueryRequest coreQueryRequest, String homePlatformId) {
        SearchClient searchClient = factory.getSearchClient();
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        QueryResponse queryResponse = searchClient.search(coreQueryRequest, false, platformIds);
        return queryResponse.getResources();
    }

    public List<FederatedResource> searchL2(PlatformRegistryQuery platformRegistryQuery, String homePlatformId) {
        PRClient prClient = factory.getPRClient(homePlatformId);
        Set<String> platformIds = new HashSet<>(Collections.singletonList(homePlatformId));
        FederationSearchResult federationSearchResult = prClient.search(platformRegistryQuery, false, platformIds);
        return federationSearchResult.getResources();
    }

    public String getResourceIdFromInternalID(String internalID, String userName, String passWord, String platformID) {
        String NO_ERROR = "NO ERROR";
        String RESOURCE_ID_NULL = "ERROR: RESOURCE ID FOUND NULL";
        String CLOUD_RESOURCE_NULL = "ERROR: CLOUD RESOURCE FOUND NULL";
        String INVALID_PLATFORM_ID = " ERROR: INVALID PLATFORM ID ";

        String ERROR_MESSAGE = NO_ERROR;
        String keystorePath = "testKeystore" + System.currentTimeMillis();
        String keystorePassword = "testKeystore";
        String exampleHomePlatformIdentifier = "SymbIoTe_Core_AAM";
        boolean checkIfIsObserved = true;//false;

        Set<String> platformIds = new HashSet<>(Collections.singletonList(exampleHomePlatformIdentifier));
        CRAMClient cramClient = null;
        RAPClient rapClient = null;
        RHClient rhClient = null;
        CloudResource cloudResource = null;

        try {
            // SearchClient searchClient   = factory.getSearchClient();
            cramClient = factory.getCramClient();
            rapClient = factory.getRapClient();
            rhClient = factory.getRHClient(platformID);
            if (rhClient == null) {
                ERROR_MESSAGE = INVALID_PLATFORM_ID + ": " + platformID;
                return null;
            }
            cloudResource = rhClient.getResource(internalID);
            // prcClient =factory.getPRClient("wew");
        } catch (Exception ex) {
            ERROR_MESSAGE = "Exception 3: " + ex.getMessage();
            if (rhClient == null)
                ERROR_MESSAGE += " rhClient is null, check platform id, ";
            if (cramClient == null)
                ERROR_MESSAGE += " cramClient is null, ";
            if (rapClient == null)
                ERROR_MESSAGE += " rapClient is null, ";
            return null;
        }

        if (cloudResource == null) {
            ERROR_MESSAGE = CLOUD_RESOURCE_NULL + ". Check the validity of internal id: " + internalID + ". Is registered ? ";
            return null;
        }

        String resourceId = cloudResource.getResource().getId();

        if (resourceId == null) {
            ERROR_MESSAGE = RESOURCE_ID_NULL;
            return null;
        }
        return resourceId;
    }

    private RdfCloudResourceList getRDFResourceList(symbioteApiRdfResource symbioteApiRdfResource) throws UnsupportedEncodingException {
        RdfCloudResourceList list = new RdfCloudResourceList();
        CloudResource cloudResource = new CloudResource();
        cloudResource.setInternalId(symbioteApiRdfResource.getRdfResource().getInternalId());
        cloudResource.setPluginId(symbioteApiRdfResource.getRdfResource().getPluginId());

        try {
            cloudResource.setAccessPolicy(symbioteApiRdfResource.getRdfResource().getAccessPolicy());
            cloudResource.setFilteringPolicy(symbioteApiRdfResource.getRdfResource().getFilteringPolicy());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        cloudResource.setResource(symbioteApiRdfResource.getRdfResource().getResource());
        list.getIdMappings().put(symbioteApiRdfResource.getRdfResource().getIdMapKey(), cloudResource);
        RDFInfo rdfInfo = new RDFInfo();
        String rdfDecoded = java.net.URLDecoder.decode(symbioteApiRdfResource.getRdfInfo().getRdf(), StandardCharsets.UTF_8.name());
        rdfInfo.setRdf(rdfDecoded);

        rdfInfo.setRdfFormat(symbioteApiRdfResource.getRdfInfo().getRdfFormat());
        list.setRdfInfo(rdfInfo);

        return list;
    }

    public ResponseEntity<?> shareResource(shareResourceModel shareResourceModel){
        RHClient rhClient = factory.getRHClient(shareResourceModel.getPlatformCredentials().getLocalPlatformId());


        Map<String, Map<String, Boolean>> toShare = new HashMap<>();

        Map<String, Boolean> resourceMap = new HashMap<>();
        resourceMap.put(shareResourceModel.getResourceInternalId(), shareResourceModel.isBartered());
        toShare.put(shareResourceModel.getFederationId(), resourceMap);
        Map<String, List<CloudResource>> result = rhClient.shareL2Resources(toShare);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<?> unShareResource(shareResourceModel unShareResourceModel){
        RHClient rhClient = factory.getRHClient(unShareResourceModel.getPlatformCredentials().getLocalPlatformId());
        List<String> resources = new ArrayList<>();
        resources.add(unShareResourceModel.getResourceInternalId());
        Map<String, List<String>> toUnShare = new HashMap<>();
        toUnShare.put(unShareResourceModel.getFederationId(), resources);
        Map<String, List<CloudResource>> result = rhClient.unshareL2Resources(toUnShare);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<?> registerL1Resource(iotFedsApiCloudResourceL1 l1Resource){
        ModelMapper modelMapper = new ModelMapper();
        CloudResource cloudResource = modelMapper.map(l1Resource.getCloudResourceL1(), CloudResource.class);

        Map<String, String> errors = new HashMap<>();
        ResourceRegistrationResponse response = new ResourceRegistrationResponse();

        try {

            RHClient rhClient = factory.getRHClient(l1Resource.getPlatformCredentials().getLocalPlatformId());
            CloudResource registeredResource = rhClient.addL1Resource(rhClient.addL1Resource(cloudResource));
            List<CloudResource> cloudResources = new ArrayList<CloudResource>();
            cloudResources.add(registeredResource);
            response.setCloudResources(cloudResources);
            // register the resource to baas
            if(baasIntegration){
                ResponseEntity baasResponse = registerResourceToBaas(l1Resource.getPlatformCredentials().getUsername(), l1Resource.getPlatformCredentials().getLocalPlatformId(),
                        registeredResource.getResource().getId());
                if(baasResponse.getStatusCode() != HttpStatus.OK)
                    errors.put("Baas error for Resource " + registeredResource.getResource().getId(), baasResponse.getBody().toString());
            }
            response.setErrors(errors);
            return new ResponseEntity<ResourceRegistrationResponse>(response , HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> registerL1RDFResource(symbioteApiRdfResource rdfResource){
        try {

            RHClient rhClient = factory.getRHClient(rdfResource.getPlatformCredentials().getLocalPlatformId());
            RdfCloudResourceList rdfCloudResourceList = getRDFResourceList(rdfResource);

            Map<String, String> errors = new HashMap<>();
            ResourceRegistrationResponse response = new ResourceRegistrationResponse();
            List<CloudResource> cloudResources = rhClient.addL1RdfResources(rdfCloudResourceList);

            response.setCloudResources(cloudResources);
            // register the resource to baas
            if(baasIntegration){
                for(CloudResource cloudResource:cloudResources){
                    ResponseEntity baasResponse = registerResourceToBaas(rdfResource.getPlatformCredentials().getUsername(), rdfResource.getPlatformCredentials().getLocalPlatformId(),
                            cloudResource.getResource().getId());
                    if(baasResponse.getStatusCode() != HttpStatus.OK)
                        errors.put("Baas error for Resource " + cloudResource.getResource().getId(), baasResponse.getBody().toString());
                }
            }
            response.setErrors(errors);
            return new ResponseEntity<ResourceRegistrationResponse>(response , HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteL1Resource(String resourceInternalId,PlatformCredentials credentials){
        Map<String, String> errors = new HashMap<>();
        ResourceRegistrationResponse response = new ResourceRegistrationResponse();

        try {

            RHClient rhClient = factory.getRHClient(credentials.getLocalPlatformId());
            CloudResource deletedResource = rhClient.deleteL1Resource(resourceInternalId);
            List<CloudResource> cloudResources = new ArrayList<CloudResource>();
            cloudResources.add(deletedResource);
            response.setCloudResources(cloudResources);
            // register the resource to baas
            if(baasIntegration){
                ResponseEntity baasResponse = deleteResourceFromBaas(deletedResource.getResource().getId());
                if(baasResponse.getStatusCode() != HttpStatus.OK)
                    errors.put("Baas error for Resource " + deletedResource.getResource().getId(), baasResponse.getBody().toString());
            }
            response.setErrors(errors);
            return new ResponseEntity<ResourceRegistrationResponse>(response , HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> registerResourceToBaas(String username, String platformId, String resourceId){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("device_id  ", resourceId);
        parameters.add("platform_id ", platformId);
        parameters.add("associated_user_id  ", username);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String body = "";
        HashMap<String, List<String>> assosiated_users = new HashMap<String, List<String>>();
        assosiated_users.put(username, new ArrayList<>());
        body = assosiated_users.toString();
        ResponseEntity<?>  response = baasClient.makeBaasHttpRequest(baasBaseUrl, registerResourceToBC, HttpMethod.PATCH , body, headers, parameters, String.class);
        return response;
    }

    private ResponseEntity<?> deleteResourceFromBaas(String resourceId){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("device_id  ", resourceId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String body = "";
        ResponseEntity<?>  response = baasClient.makeBaasHttpRequest(baasBaseUrl, deleteResourceFromBC, HttpMethod.DELETE , body, headers, parameters, String.class);
        return response;
    }

}
