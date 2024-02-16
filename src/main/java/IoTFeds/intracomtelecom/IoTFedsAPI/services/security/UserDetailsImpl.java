//package IoTFeds.intracomtelecom.IoTFedsAPI.services.security;
//
//import IoTFeds.intracomtelecom.IoTFedsAPI.payload.User;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public class UserDetailsImpl implements UserDetails {
//
//    private String username;
//
//    @JsonIgnore
//    private String password;
//
//    private String localPlatformId;
//
//    private String clientId;
//
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public UserDetailsImpl(String username, String password, String localPlatformId, String clientId, Collection<? extends GrantedAuthority> authorities) {
//        this.username = username;
//        this.password = password;
//        this.clientId = clientId;
//        this.localPlatformId = localPlatformId;
//        this.authorities = authorities;
//
//    }
//
//    public static UserDetailsImpl build(User user) {
//
//        return new UserDetailsImpl(
//                user.getUsername(),
//                user.getPassword(),
//                user.getLocalPlatformId(),
//                user.getClientId(),
//                new ArrayList<>()
//        );
//    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    public String getLocalPlatformId() {
//        return localPlatformId;
//    }
//
//    public String getClientId() {
//        return clientId;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o)
////            return true;
////        if (o == null || getClass() != o.getClass())
////            return false;
////        UserDetailsImpl user = (UserDetailsImpl) o;
////        return Objects.equals(id, user.id);
////    }
//}
