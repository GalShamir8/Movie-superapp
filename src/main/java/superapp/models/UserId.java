package superapp.models;


public class UserId {
    private String superapp;
    private String email;

    public UserId() { }

    public UserId(String superApp, String email) {
        this.superapp = superApp;
        this.email = email;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
