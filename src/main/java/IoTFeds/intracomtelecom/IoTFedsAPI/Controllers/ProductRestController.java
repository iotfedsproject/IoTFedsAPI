package IoTFeds.intracomtelecom.IoTFedsAPI.controllers;

import IoTFeds.intracomtelecom.IoTFedsAPI.models.PlatformCredentials;
import IoTFeds.intracomtelecom.IoTFedsAPI.models.product.SymbioteApiProduct;
import IoTFeds.intracomtelecom.IoTFedsAPI.models.symbioteApiPlatformInfo;
import IoTFeds.intracomtelecom.IoTFedsAPI.utilities.login.SymbIoTeLogin;
import IoTFeds.intracomtelecom.IoTFedsAPI.utilities.product.ProductService;
import IoTFeds.intracomtelecom.IoTFedsAPI.utilities.resource.ResourceService;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory;
import eu.h2020.symbiote.model.cim.Observation;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("symbioteapi/products")
public class ProductRestController {

    @Value("${configuration.keystorePath}")
    private String keystorePath;

    @Autowired
    SymbIoTeLogin login;




    @ApiOperation(value = "End-point to access product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful access", response = Observation.class)})
    @RequestMapping(path = "/access/product/", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AccessL1Resource(@RequestBody SymbioteApiProduct symbioteApiProduct) {
        try {
            PlatformCredentials credentials = symbioteApiProduct.getPlatformCredentials();

            AbstractSymbIoTeClientFactory factory = login.GetSymbIoTeFactory(credentials);
            ProductService productService = new ProductService(factory);
            return productService.accessProduct(symbioteApiProduct);
        } catch (SecurityHandlerException e) {
            return new ResponseEntity<String>(e.getErrorMessage(), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}

