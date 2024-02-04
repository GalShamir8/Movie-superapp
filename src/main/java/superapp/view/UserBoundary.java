package superapp.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import superapp.common.Helper;
import superapp.models.UserId;

import java.util.Date;
import java.util.Map;

public class UserBoundary {
   private String role;
   private String avatar;
   private String username;
   private UserId userId;
   private Date createdAt;

    private ObjectMapper mapper;

    public UserBoundary() { }
    public UserBoundary(String role, String avatar, String username, UserId userId, Date createdAt) {
        mapper = new ObjectMapper();
        this.role = role;
        this.avatar = avatar;
        this.username = username;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
