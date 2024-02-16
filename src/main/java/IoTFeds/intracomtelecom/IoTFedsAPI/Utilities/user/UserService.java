package IoTFeds.intracomtelecom.IoTFedsAPI.utilities.user;

import IoTFeds.intracomtelecom.IoTFedsAPI.models.UserDeletionDetails;
import IoTFeds.intracomtelecom.IoTFedsAPI.models.UserRegistrationDetails;
import IoTFeds.intracomtelecom.IoTFedsAPI.services.baas.BaasClient;
import IoTFeds.intracomtelecom.IoTFedsAPI.utilities.resource.ResourceService;
import eu.h2020.symbiote.client.AbstractSymbIoTeClientFactory;
import eu.h2020.symbiote.security.commons.enums.AccountStatus;
import eu.h2020.symbiote.security.commons.enums.ManagementStatus;
import eu.h2020.symbiote.security.commons.enums.OperationType;
import eu.h2020.symbiote.security.commons.enums.UserRole;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.communication.IAAMClient;
import eu.h2020.symbiote.security.communication.payloads.Credentials;
import eu.h2020.symbiote.security.communication.payloads.UserDetails;
import eu.h2020.symbiote.security.communication.payloads.UserManagementRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

public class UserService {
    private AbstractSymbIoTeClientFactory factory;
    private Properties properties =  new Properties();
    private Boolean baasIntegration;
    private String registerUserToBC;
    private String deleteUserToBC;
    private String baasBaseUrl;
    private int connectTimeoutSeconds;
    private BaasClient baasClient;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(AbstractSymbIoTeClientFactory clientFactory) {
        factory = clientFactory;
        InputStream inputStream = ResourceService.class
                .getClassLoader()
                .getResourceAsStream("baas.properties");
        try {
            properties.load(inputStream);
            inputStream.close();
            baasIntegration = Boolean.parseBoolean(properties.getProperty("baas.integration"));
            registerUserToBC = properties.getProperty("baas.registerUser.url");
            deleteUserToBC = properties.getProperty("baas.deleteUser.url");
            baasBaseUrl = properties.getProperty("baas.baseUrl");
            connectTimeoutSeconds = Integer.parseInt(properties.getProperty("baas.connectTimeoutSeconds"));
            RestTemplateBuilder builder = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds));
            baasClient = new BaasClient(builder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ManagementStatus registerUser(UserRegistrationDetails details) throws AAMException {
        IAAMClient iaamClient = factory.getAAMClient(details.getPlatformId());
        UserManagementRequest userManagementRequest = new UserManagementRequest(
                new Credentials(details.getAam_owner_username(), details.getAam_owner_password()),
                new Credentials(details.getNew_username(), details.getNew_password()),
                new UserDetails(
                        new Credentials(details.getNew_username(), details.getNew_password()),
                        details.getEmail(),
                        UserRole.USER,
                        AccountStatus.ACTIVE,
                        details.getAttributes(),
                        new HashMap<>(),
                        true,
                        false),
                OperationType.CREATE);
        ManagementStatus status = iaamClient.manageUser(userManagementRequest);
        return status;
    }

    public ManagementStatus deleteUser(UserDeletionDetails details) throws AAMException {

        IAAMClient iaamClient = factory.getAAMClient(details.getPlatformId());
        UserManagementRequest userDeleteRequest = new UserManagementRequest(
                new Credentials(details.getAam_owner_username(), details.getAam_owner_password()),
                new Credentials(),
                new UserDetails(
                        new Credentials(details.getDelete_username(), null),
                        "",
                        UserRole.USER,
                        AccountStatus.ACTIVE,
                        new HashMap<>(),
                        new HashMap<>(),
                        true,
                        false
                ),
                OperationType.DELETE
        );
        ManagementStatus status = iaamClient.manageUser(userDeleteRequest);
        return status;
    }

    private ResponseEntity<?> registerUserToBaas(String id, String role, String mail, String organization){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        HashMap<String, String> body = new HashMap();
        body.put("id ", id);
        body.put("role", role);
        body.put("mail ", mail);
        body.put("organization ", organization);
        return baasClient.makeBaasHttpRequest(baasBaseUrl, registerUserToBC, HttpMethod.POST, body, parameters);
    }

    private ResponseEntity<?> deleteUserToBaas(String userId) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        HashMap<String, String> body = new HashMap<>();
        body.put("user_id", userId);
        return baasClient.makeBaasHttpRequest(baasBaseUrl, deleteUserToBC, HttpMethod.DELETE, body, parameters);
    }
}
