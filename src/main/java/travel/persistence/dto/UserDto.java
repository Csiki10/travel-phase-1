package travel.persistence.dto;
import travel.domain.Role;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String name;
    private String email;
    private Role role;
    private CredentialsDto credentials;
}
