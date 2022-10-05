package travel.persistence.dto;

import lombok.Data;
import travel.domain.Role;

@Data
public class UserDto {
    private long id;
    private String name;
    private String email;
    private Role role;
    private CredentialsDto credentials;
}
