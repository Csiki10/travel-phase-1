package travel.domain;

public class User {
    private long id;
    private String fullName;
    private String email;
    private Role role;
    private Credentials credential;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Credentials getCredential() {
        return credential;
    }

    public void setCredential(Credentials credential) {
        this.credential = credential;
    }


}
