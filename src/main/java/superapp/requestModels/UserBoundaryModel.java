package superapp.requestModels;

import java.util.Map;

public class UserBoundaryModel {
    private String email;
    private String avatar;
    private String username;
    private String role;
    private Map<String,Object> details ;

    public UserBoundaryModel() { }

    public Map<String,Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String,Object> details) {
        this.details = details;
    }

    public UserBoundaryModel(String email, String avatar, String username, String role, Map<String,Object> details ) {
        this.email = email;
        this.avatar = avatar;
        this.username = username;
        this.role = role;
        this.details = details;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
