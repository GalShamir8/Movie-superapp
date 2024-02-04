package superapp.requestModels;

import superapp.models.UserId;

import java.util.Map;

public class ObjectBoundaryModel {
    private String type;
    private String alias;
    private boolean active;
    private UserId createdBy;
    private Map<String, Object> objectDetails;

    public ObjectBoundaryModel() { }

    public ObjectBoundaryModel(String type, String alias, boolean active, UserId userId, Map<String, Object> objectDetails) {
        this.type = type;
        this.alias = alias;
        this.active = active;
        this.createdBy = userId;
        this.objectDetails = objectDetails;
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
