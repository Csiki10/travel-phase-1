package travel.domain;
import lombok.Data;

@Data
public class User {
    private long id;
    private String fullName;
    private String email;
    private Role role;
    private Credentials credential;
}
