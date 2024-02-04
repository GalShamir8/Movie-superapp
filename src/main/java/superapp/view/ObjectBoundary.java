package superapp.view;

import superapp.data.UserEntity;
import superapp.models.ObjectId;
import superapp.models.UserId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectBoundary {


    private ObjectId objectId;
    private String type;
    private String alias;
    private boolean active;
    private Date createdAt;
    private UserId createdBy;

    private Map<String, Object> objectDetails;


    public ObjectBoundary(
            ObjectId objectId,
            String type,
            String alias,
            boolean active,
            Date createdAt,
            UserId createdBy,
            Map<String, Object> objectDetails) {
        this.objectId = objectId;
        this.type = type;
        this.alias = alias;
        this.active = active;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.objectDetails = objectDetails;
    }

    public ObjectBoundary() {
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserId getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserId createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }
}


