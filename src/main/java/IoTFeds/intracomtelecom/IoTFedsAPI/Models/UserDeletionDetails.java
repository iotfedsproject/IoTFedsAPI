package IoTFeds.intracomtelecom.IoTFedsAPI.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDeletionDetails {

    private String delete_username;
    private String aam_owner_username;
    private String aam_owner_password;
    private String platformId;
}