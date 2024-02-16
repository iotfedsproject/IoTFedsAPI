package IoTFeds.intracomtelecom.IoTFedsAPI.controllers;

import IoTFeds.intracomtelecom.IoTFedsAPI.models.UserDeletionDetails;
import IoTFeds.intracomtelecom.IoTFedsAPI.models.UserRegistrationDetails;
import IoTFeds.intracomtelecom.IoTFedsAPI.utilities.login.SymbIoTeLogin;
import IoTFeds.intracomtelecom.IoTFedsAPI.utilities.user.UserService;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory;
import eu.h2020.symbiote.security.commons.enums.ManagementStatus;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("symbioteapi/users")
public class UserRestController {
    @Autowired
    SymbIoTeLogin login;

    @ApiOperation(value = "End-point to register a user to symbiote")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful registration", response = ManagementStatus.class)})
    @RequestMapping(path = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDetails details) {
        AbstractSymbIoTeClientFactory factory = null;

        try {
            factory = login.GetSymbIoTeCoreFactory();
            UserService service = new UserService(factory);
            return new ResponseEntity<ManagementStatus>(service.registerUser(details), HttpStatus.OK);
        } catch (SecurityHandlerException | AAMException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<List<String>>(Arrays.asList(e.getErrorMessage(), e.getMessage()), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "End-point to delete a user to symbiote")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful deletion", response = ManagementStatus.class)})
    @RequestMapping(path = "/deleteUser", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@RequestBody UserDeletionDetails details) {
        AbstractSymbIoTeClientFactory factory = null;

        try {
            factory = login.GetSymbIoTeCoreFactory();
            UserService service = new UserService(factory);
            return new ResponseEntity<ManagementStatus>(service.deleteUser(details), HttpStatus.OK);
        } catch (SecurityHandlerException | AAMException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<List<String>>(Arrays.asList(e.getErrorMessage(), e.getMessage()), e.getStatusCode());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
