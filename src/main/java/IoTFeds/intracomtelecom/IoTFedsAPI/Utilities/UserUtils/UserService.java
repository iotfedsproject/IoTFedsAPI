package IoTFeds.intracomtelecom.IoTFedsAPI.Utilities.UserUtils;

import IoTFeds.intracomtelecom.IoTFedsAPI.Models.UserDeletionDetails;
import IoTFeds.intracomtelecom.IoTFedsAPI.Models.UserRegistrationDetails;
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

import java.security.Principal;
import java.util.HashMap;

public class UserService {
    private AbstractSymbIoTeClientFactory factory;

    public UserService(AbstractSymbIoTeClientFactory clientFactory) {
        factory = clientFactory;
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
                        new HashMap<>(),
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
        return iaamClient.manageUser(userDeleteRequest);
    }
}
