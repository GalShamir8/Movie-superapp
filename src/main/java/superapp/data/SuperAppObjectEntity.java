package superapp.data;


import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "MOVIE_SUPER_APPS")
public class SuperAppObjectEntity {
    private String type;
    private String objectId;
    private String alias;
    private boolean active;
    private Date createdAt;
    private String details;
    private String userId;
    private SuperAppObjectEntity parentObject;
    private Set<SuperAppObjectEntity> childObjects;
    private Set<UserEntity> users;

    public SuperAppObjectEntity() {
        this.childObjects = new HashSet<>();
    }

    public SuperAppObjectEntity(String type, String objectId, String alias, boolean active, Date createdAt, String details, String userId) {
        this.type = type;
        this.objectId = objectId;
        this.alias = alias;
        this.active = active;
        this.createdAt = createdAt;
        this.details = details;
        this.userId = userId;
        this.childObjects = new HashSet<>();
        this.users = new HashSet<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Id
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Lob
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ManyToOne(fetch = FetchType.LAZY /*optional = true*/ /*cascade = */)
    public SuperAppObjectEntity getParentObject() {
        return parentObject;
    }

    public void setParentObject(SuperAppObjectEntity parentObject) {
        this.parentObject = parentObject;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentObject" /*cascade = */)
    public Set<SuperAppObjectEntity> getChildObjects() {
        return childObjects;
    }

    public void setChildObjects(Set<SuperAppObjectEntity> childObjects) {
        this.childObjects = childObjects;
    }

    public void addChildObject(SuperAppObjectEntity childObject) {
        this.childObjects.add(childObject);
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade =  CascadeType.PERSIST)
    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public void addUser(UserEntity user){
        users.add(user);
    }

    public void deleteAllUsers(){
        for (UserEntity user: users){
            user.getFavorites().remove(this);
        }
        users.clear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.objectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SuperAppObjectEntity other = (SuperAppObjectEntity) obj;
        return Objects.equals(this.objectId, other.getObjectId());
    }
}
