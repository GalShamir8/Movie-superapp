package superapp.myMovies.models;

public class RequestModel {

    String email ;
    String superAppName ;
    String objectId ;

    public RequestModel(String email, String superAppName, String objectId) {
        this.email = email;
        this.superAppName = superAppName;
        this.objectId = objectId;
    }

    public RequestModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSuperAppName() {
        return superAppName;
    }

    public void setSuperAppName(String superAppName) {
        this.superAppName = superAppName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
