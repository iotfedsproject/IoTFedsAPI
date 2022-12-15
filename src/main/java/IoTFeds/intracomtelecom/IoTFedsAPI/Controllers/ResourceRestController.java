package IoTFeds.intracomtelecom.IoTFedsAPI.Controllers;

import IoTFeds.intracomtelecom.IoTFedsAPI.Models.*;
import IoTFeds.intracomtelecom.IoTFedsAPI.Models.ResourceRegistration.ResourceRegistrationResponse;
import IoTFeds.intracomtelecom.IoTFedsAPI.Utilities.Login.SymbIoTeLogin;
import IoTFeds.intracomtelecom.IoTFedsAPI.Utilities.ResourceUtils.ResourceService;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory;
import eu.h2020.symbiote.client.interfaces.RHClient;
import eu.h2020.symbiote.cloud.model.internal.CloudResource;
import eu.h2020.symbiote.cloud.model.internal.FederatedResource;
import eu.h2020.symbiote.cloud.model.internal.RdfCloudResourceList;
import eu.h2020.symbiote.cloud.model.internal.FederationSearchResult;
import eu.h2020.symbiote.cloud.model.internal.PlatformRegistryQuery;
import eu.h2020.symbiote.core.ci.QueryResourceResult;
import eu.h2020.symbiote.core.internal.CoreQueryRequest;
import eu.h2020.symbiote.model.cim.Observation;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.security.communication.payloads.SecurityRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("symbioteapi/resources")
public class ResourceRestController {

    @Value("${configuration.keystorePath}")
    private String keystorePath;

    @Autowired
    SymbIoTeLogin login;

    @ApiOperation(value = "End-point to get L1 resource for a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful get", response = CloudResource.class)})
    @RequestMapping(path = "/l1/{resourceInternalId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCloudResource(@ApiParam(value = "The internal Id of the resource to be retrieved, provided by the user during registration.", example = "isen1") @PathVariable String resourceInternalId, @RequestBody symbioteApiPlatformInfo symbioteApiPlatformInfo) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(symbioteApiPlatformInfo.getPlatformCredentials());
            RHClient rhClient = factory.getRHClient(symbioteApiPlatformInfo.getRemotePlatformId());
            return new ResponseEntity<CloudResource>(rhClient.getResource(resourceInternalId), HttpStatus.OK);
        } catch (SecurityHandlerException | NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @ApiOperation(value = "End-point to get a list of L1 resources for a specific platform Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful get", response = CloudResource.class, responseContainer = "List")})
    @RequestMapping(path = "/getall/L1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListL1Resources(@RequestBody symbioteApiPlatformInfo symbioteApiPlatformInfo) {

        AbstractSymbIoTeClientFactory factory = null;

        try {
            factory = login.GetSymbIoTeFactory(symbioteApiPlatformInfo.getPlatformCredentials());
            ResourceService service = new ResourceService(factory);
            return new ResponseEntity<List<QueryResourceResult>>(service.getListL1(symbioteApiPlatformInfo.getPlatformCredentials().getLocalPlatformId(), symbioteApiPlatformInfo.getRemotePlatformId()), HttpStatus.OK);
        } catch (SecurityHandlerException | NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @ApiOperation(value = "End-point to get L2 resource for a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful get", response = CloudResource.class)})
    @RequestMapping(path = "/l2/{resourceInternalId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getL2Resource(@PathVariable String resourceInternalId, @RequestBody symbioteApiFederationInfo federationInfo) {

        AbstractSymbIoTeClientFactory factory = null;
        try {

            factory = login.GetSymbIoTeFactory(federationInfo.getPlatformCredentials());
            ResourceService service = new ResourceService(factory);
            return new ResponseEntity<FederatedResource>(service.getL2Resource(federationInfo.getPlatformCredentials().getLocalPlatformId(), resourceInternalId, federationInfo.getFederationId()), HttpStatus.OK);
        } catch (SecurityHandlerException | NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @ApiOperation(value = "End-point to get a list of L2 resources for a specific platform")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful get", response = CloudResource.class, responseContainer = "List")})
    @RequestMapping(path = "/getall/l2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListL2Resources(@RequestBody symbioteApiPlatformInfo platformInfo) {

        AbstractSymbIoTeClientFactory factory = null;
        try {

            factory = login.GetSymbIoTeFactory(platformInfo.getPlatformCredentials());
            ResourceService service = new ResourceService(factory);
            return new ResponseEntity<List<FederatedResource>>(service.getListL2(platformInfo.getPlatformCredentials().getLocalPlatformId(), platformInfo.getRemotePlatformId()), HttpStatus.OK);
        } catch (SecurityHandlerException | NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @ApiOperation(value = "End-point to search for resources that match specific criteria")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful search", response = QueryResourceResult.class, responseContainer = "List")})
    @RequestMapping(path = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchResource(@RequestBody PlatformCredentials credentials, @ApiParam(value = "The platform id the user wishes to search in.", example = "icom-platform") @RequestParam(required = false) String platform_id,
                                            @ApiParam(value = "The platform name the user wishes to search in.", example = "icom-platform") @RequestParam(required = false) String platform_name,
                                            @ApiParam(value = "The owner of the resource.", example = "icom") @RequestParam(required = false) String owner,
                                            @ApiParam(value = "The name of the resource.", example = "dimmer rgb sensor") @RequestParam(required = false) String name,
                                            @ApiParam(value = "Auto generated by SymbIoTe id, should the user know it and wants to search with it.", example = "593943aab4f8e209390e9422") @RequestParam(required = false) String id,
                                            @ApiParam(value = "Description of the resource", example = "rgb light") @RequestParam(required = false) String description,
                                            @ApiParam(value = "Location of the resource", example = "Paiania") @RequestParam(required = false) String location_name,
                                            @ApiParam(value = "Location latitude of the resource", example = "43.6732994") @RequestParam(required = false) Double location_lat,
                                            @ApiParam(value = "Location longitude of the resource", example = "10.34831059999999") @RequestParam(required = false) Double location_long,
                                            @RequestParam(required = false) Integer max_distance,
                                            @ApiParam(value = "List of observed properties", example = "[luminous efficacy,luminous intensity,luminous flux,luminous exposure]") @RequestParam(required = false) List<String> observed_property,
                                            @ApiParam(value = "List of observed properties iris") @RequestParam(required = false, value = "") List<String> observed_property_iri,
                                            @ApiParam(value = "Link for model of resource type", example = "http://www.symbiote-h2020.eu/ontology/core#StationarySensor") @RequestParam(required = false) String resource_type,
                                            @RequestParam(required = false) SecurityRequest securityRequest,
                                            @RequestParam(required = false) Boolean should_rank) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(credentials);
            CoreQueryRequest coreQueryRequest = new CoreQueryRequest(platform_id, platform_name, owner, name,
                    id, description, location_name, location_lat,
                    location_long, max_distance, observed_property,
                    observed_property_iri, resource_type,
                    securityRequest, should_rank);
//            coreQueryRequest = CoreQueryRequest.newInstance(coreQueryRequest);
            ResourceService service = new ResourceService(factory);
            return new ResponseEntity<List<QueryResourceResult>>(service.search(coreQueryRequest, credentials.getLocalPlatformId()), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to search for L2 resources that match specific criteria")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful search", response = FederatedResource.class, responseContainer = "List")})
    @RequestMapping(path = "/search/l2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchResourceForL2(@RequestBody PlatformCredentials credentials,
                                                 @ApiParam(value = "A list with the resource names") @RequestParam("name") List<String> name,
                                                 @ApiParam(value = "The resource description") @RequestParam("description") List<String> description,
                                                 @ApiParam(value = "The id for identifying the resource in the symbIoTe federation (aggregationId)") @RequestParam("id") List<String> id,
                                                 @ApiParam(value = "The list of federation ids") @RequestParam("federationId") List<String> federationId,
                                                 @ApiParam(value = "Property observed by resource (sensor). Can indicate more than one observed property") @RequestParam("observes_property") List<String> observes_property,
                                                 @ApiParam(value = "Type of queried resource") @RequestParam("resource_type") String resource_type,
                                                 @ApiParam(value = "Name of resource location; can be a set of different locations") @RequestParam("location_name") List<String> location_name,
                                                 @ApiParam(value = "Latitude of resource location. It concerns WGS84 locations for devices") @RequestParam("location_lat") Double location_lat,
                                                 @ApiParam(value = "Longitude of resource location. It concerns WGS84 locations for devices") @RequestParam("location_long") Double location_long,
                                                 @ApiParam(value = "maximal distance from specified resource latitude and longitude (in meters)") @RequestParam("max_distance") Double max_distance,
                                                 @RequestParam("resource_trust") Double resource_trust,
                                                 @RequestParam("adaptive_trust") Double adaptive_trust,
                                                 @ApiParam(value = "the field to be used for sorting the resources") @RequestParam("sort") String sort) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(credentials);
            PlatformRegistryQuery platformRegistryQuery = new PlatformRegistryQuery(
                    name,
                    description,
                    id,
                    federationId,
                    observes_property,
                    resource_type,
                    location_name,
                    location_lat,
                    location_long,
                    max_distance,
                    resource_trust,
                    adaptive_trust,
                    sort
            );
            ResourceService service = new ResourceService(factory);
            return new ResponseEntity<List<FederatedResource>>(service.searchL2(platformRegistryQuery, credentials.getLocalPlatformId()), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to add L1 resource with a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful add", response = ResourceRegistrationResponse.class)})
    @RequestMapping(path = "/add/l1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addL1Resource(@RequestBody iotFedsApiCloudResourceL1 l1Resource) {

        AbstractSymbIoTeClientFactory factory = null;
        try {

            factory = login.GetSymbIoTeFactory(l1Resource.getPlatformCredentials());
            ResourceService resourceService = new ResourceService(factory);
            return resourceService.registerL1Resource(l1Resource);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<List<String>>(Arrays.asList(e.getErrorMessage(), e.getMessage()), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to add L1 resource with a specific Id using RDF. The RDF string should be URL UTF8 encoded")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful add", response = CloudResource.class)})
    @RequestMapping(path = "/add/l1RDF", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addL1RDFResource(@RequestBody symbioteApiRdfResource rdfResource) {

        AbstractSymbIoTeClientFactory factory = null;
        try {

            factory = login.GetSymbIoTeFactory(rdfResource.getPlatformCredentials());
            ResourceService service = new ResourceService(factory);
            return service.registerL1RDFResource(rdfResource);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<List<String>>(Arrays.asList(e.getErrorMessage(), e.getMessage()), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to delete L1 resource with a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful deletion", response = ResourceRegistrationResponse.class)})
    @RequestMapping(path = "/delete/l1/{resourceInternalId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteL1Resource(@ApiParam(value = "The internal Id of the resource to be retrieved, provided by the user during registration.", example = "isen1") @PathVariable String resourceInternalId, @RequestBody PlatformCredentials credentials) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(credentials);
            ResourceService service = new ResourceService(factory);
            return service.deleteL1Resource(resourceInternalId, credentials);
        } catch (SecurityHandlerException | NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to update L1 resource with a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful add", response = CloudResource.class)})
    @RequestMapping(path = "/update/l1", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateL1Resource(@RequestBody symbioteApiCloudResource l1Resource) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(l1Resource.getPlatformCredentials());
            RHClient rhClient = factory.getRHClient(l1Resource.getPlatformCredentials().getLocalPlatformId());
            return new ResponseEntity<CloudResource>(rhClient.updateL1Resource(l1Resource.getCloudResource()), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @ApiOperation(value = "End-point to add L2 resource with a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful add", response = CloudResource.class, responseContainer = "List")})
    @RequestMapping(path = "/add/l2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addL2Resource(@RequestBody symbioteApiCloudResource l1Resource) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(l1Resource.getPlatformCredentials());
            RHClient rhClient = factory.getRHClient(l1Resource.getPlatformCredentials().getLocalPlatformId());
            return new ResponseEntity<List<CloudResource>>(rhClient.addL2Resources(Collections.singletonList(l1Resource.getCloudResource())), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to remove L2 resource with a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful deletion", response = CloudResource.class)})
    @RequestMapping(path = "/delete/l2/{resourceInternalId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteL2Resource(@PathVariable String resourceInternalId, @RequestBody PlatformCredentials credentials) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(credentials);
            RHClient rhClient = factory.getRHClient(credentials.getLocalPlatformId());
            List<String> removedL2Resources = rhClient.removeL2Resources(Collections.singletonList(resourceInternalId));
            return new ResponseEntity<CloudResource>(HttpStatus.OK);
        } catch (SecurityHandlerException | NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to update L2 resource with a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful add", response = CloudResource.class, responseContainer = "List")})
    @RequestMapping(path = "/update/l2", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateL2Resource(@RequestBody symbioteApiCloudResource l1Resource) {

        AbstractSymbIoTeClientFactory factory = null;

        try {

            factory = login.GetSymbIoTeFactory(l1Resource.getPlatformCredentials());
            RHClient rhClient = factory.getRHClient(l1Resource.getPlatformCredentials().getLocalPlatformId());
            return new ResponseEntity<List<CloudResource>>(rhClient.updateL2Resources(Collections.singletonList(l1Resource.getCloudResource())), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to access L1 resource with a specific resource internal Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful access", response = Observation.class)})
    @RequestMapping(path = "/access/l1/{resourceInternalId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AccessL1Resource(@ApiParam(value = "The internal Id of the resource to be retrieved, provided by the user during registration.", example = "isen1") @PathVariable String resourceInternalId, @RequestBody symbioteApiPlatformInfo platformnfo) {
        try {
            PlatformCredentials credentials = platformnfo.getPlatformCredentials();

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeFactory(credentials);
            ResourceService resourceService = new ResourceService(factory);
            return new ResponseEntity<Observation>(resourceService.accessL1Resource(platformnfo.getPlatformCredentials().getLocalPlatformId(), platformnfo.getRemotePlatformId(), resourceInternalId), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to access L1 resource (actuator) with a specific resource internal Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful access", response = String.class)})
    @RequestMapping(path = "/access/l1/actuator/{resourceInternalId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AccessL1ResourceActuator(
            @ApiParam(value = "The internal Id of the resource to be retrieved, provided by the user during registration.", example = "isen1") @PathVariable String resourceInternalId,
            @RequestBody SymbioteApiPlatformInfoActuator symbioteApiPlatformInfo) {
        try {
            PlatformCredentials credentials = symbioteApiPlatformInfo.getPlatformCredentials();

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeFactory(credentials);
            ResourceService resourceService = new ResourceService(factory);
            resourceService.accessL1ResourceActuator(symbioteApiPlatformInfo.getPlatformCredentials().getLocalPlatformId(), symbioteApiPlatformInfo.getBody(), symbioteApiPlatformInfo.getRemotePlatformId(), resourceInternalId);
            return new ResponseEntity<String>("Successfully actuated with body: [" + symbioteApiPlatformInfo.getBody() + "]", HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to access L1 resource (service) with a specific resource internal Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful access", response = String.class)})
    @RequestMapping(path = "/access/l1/service/{resourceInternalId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AccessL1ResourceInvokeService(
            @ApiParam(value = "The internal Id of the resource to be retrieved, provided by the user during registration.", example = "isen1") @PathVariable String resourceInternalId,
            @RequestBody SymbioteApiPlatformInfoActuator symbioteApiPlatformInfo) {
        try {
            PlatformCredentials credentials = symbioteApiPlatformInfo.getPlatformCredentials();

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeFactory(credentials);
            ResourceService resourceService = new ResourceService(factory);
            resourceService.accessL1ResourceActuator(symbioteApiPlatformInfo.getPlatformCredentials().getLocalPlatformId(), symbioteApiPlatformInfo.getBody(), symbioteApiPlatformInfo.getRemotePlatformId(), resourceInternalId);
            return new ResponseEntity<String>("Successfully invoked service with body: [" + symbioteApiPlatformInfo.getBody() + "]", HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to access L1 resource with a specific resource Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful access", response = Observation.class)})
    @RequestMapping(path = "/access/l1/Id/{resourceId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AccessL1ResourceWithResourceId(@ApiParam(value = "The Id of the resource to be retrieved, provided by SymbIoTe during registration.", example = "62c591280f160e00013b7154") @PathVariable String resourceId, @RequestBody symbioteApiPlatformInfo platformnfo) {
        try {
            PlatformCredentials credentials = platformnfo.getPlatformCredentials();

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeFactory(credentials);
            ResourceService resourceService = new ResourceService(factory);
            return new ResponseEntity<Observation>(resourceService.accessL1ResourceWithId(platformnfo.getPlatformCredentials().getLocalPlatformId(), platformnfo.getRemotePlatformId(), resourceId), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @ApiOperation(value = "End-point to access L2 resource with a specific Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful access", response = Observation.class)})
    @RequestMapping(path = "/access/l2/{resourceInternalId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AccessL2Resource(@PathVariable String resourceInternalId, @RequestBody symbioteApiFederationInfo federationInfo) {
        try {
            PlatformCredentials credentials = federationInfo.getPlatformCredentials();

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeFactory(credentials);
            ResourceService resourceService = new ResourceService(factory);
            return new ResponseEntity<Observation>(resourceService.accessL2Resource(federationInfo.getPlatformCredentials().getLocalPlatformId(), resourceInternalId, federationInfo.getFederationId()), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }


        // Get the necessary component clients
        /*
        Search for resources in the Platform Registry of the specified platform
         */

        // Create the request
//        CoreQueryRequest coreQueryRequest = new CoreQueryRequest.Builder()
//                .platformId(platformId).id(resourceId)
//                .build();
//        QueryResponse queryResponse = searchClient.search(coreQueryRequest,false, platformIds);
//        List<QueryResourceResult> result = queryResponse.getResources().stream()
//                .filter(resource -> resource.getId().equals(resourceId)).collect(Collectors.toList());
//        ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
//        String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);

//        Observation observation = rapClient.getLatestObservation(resourceUrl, false, platformIds);
    }

    @ApiOperation(value = "End-point to delete all resources within a specific platform")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful deletion", response = String.class)})
    @RequestMapping(path = "/deleteall", method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> DeleteAllResources(@RequestBody PlatformCredentials credentials) {
        try {

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeFactory(credentials);
            RHClient rhClient = factory.getRHClient(credentials.getLocalPlatformId());
            rhClient.clearResources();
            return new ResponseEntity<String>("Deleted all resources", HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }


        // Get the necessary component clients
        /*
        Search for resources in the Platform Registry of the specified platform
         */

        // Create the request
//        CoreQueryRequest coreQueryRequest = new CoreQueryRequest.Builder()
//                .platformId(platformId).id(resourceId)
//                .build();
//        QueryResponse queryResponse = searchClient.search(coreQueryRequest,false, platformIds);
//        List<QueryResourceResult> result = queryResponse.getResources().stream()
//                .filter(resource -> resource.getId().equals(resourceId)).collect(Collectors.toList());
//        ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
//        String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);

//        Observation observation = rapClient.getLatestObservation(resourceUrl, false, platformIds);
    }

    @ApiOperation(value = "End-point to return the id of a cloudresource, provided that the caller knows the platform id and internal id of the resource")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful Search for id", response = String.class)})
    @RequestMapping(path = "/getResourceIdFromInternalID", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getResourceIdFromInternalID(@RequestBody resourceInfo resourceInfo) {
        try {

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeCoreFactory();
            ResourceService resourceService = new ResourceService(factory);
            return new ResponseEntity<String>(resourceService.getResourceIdFromInternalID(resourceInfo.getInternalId(), "", "", resourceInfo.getPlatformId()), HttpStatus.OK);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        // Get the necessary component clients
        /*
        Search for resources in the Platform Registry of the specified platform
         */

        // Create the request
//        CoreQueryRequest coreQueryRequest = new CoreQueryRequest.Builder()
//                .platformId(platformId).id(resourceId)
//                .build();
//        QueryResponse queryResponse = searchClient.search(coreQueryRequest,false, platformIds);
//        List<QueryResourceResult> result = queryResponse.getResources().stream()
//                .filter(resource -> resource.getId().equals(resourceId)).collect(Collectors.toList());
//        ResourceUrlsResponse resourceUrlsResponse = cramClient.getResourceUrl(resourceId, false, platformIds);
//        String resourceUrl = resourceUrlsResponse.getBody().get(resourceId);

//        Observation observation = rapClient.getLatestObservation(resourceUrl, false, platformIds);
    }

    @ApiOperation(value = "End-point to share a resource to a federation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully shared", response = String.class)})
    @RequestMapping(path = "/shareresource", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> shareResource(@RequestBody shareResourceModel shareResourceModel) {
        try {

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeCoreFactory();
            ResourceService resourceService = new ResourceService(factory);
            return resourceService.shareResource(shareResourceModel);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to unshare a resource from a federation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully unshared", response = String.class)})
    @RequestMapping(path = "/unshareresource", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> unShareResource(@RequestBody shareResourceModel shareResourceModel) {
        try {

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeCoreFactory();
            ResourceService resourceService = new ResourceService(factory);
            return resourceService.unShareResource(shareResourceModel);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}


