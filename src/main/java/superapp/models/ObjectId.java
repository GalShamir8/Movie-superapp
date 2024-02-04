package superapp.models;

import static superapp.common.Helper.getSuperAppDomain;


public class ObjectId {
    private String superApp;

    private String internalObjectId;

    public ObjectId() { }

    public ObjectId(String superApp, String internalObjectId) {
        this.superApp = superApp;
        this.internalObjectId = internalObjectId;
    }

    public String getSuperApp() {
        return superApp;
    }

    public void setSuperApp(String superApp) {
        this.superApp = superApp;
    }

    public String getInternalObjectId() {
        return internalObjectId;
    }

    public void setInternalObjectId(String internalObjectId) {
        this.internalObjectId = internalObjectId;
    }
    public boolean equals(ObjectId oId){
        if(this.getInternalObjectId().equals(oId.getInternalObjectId()))
            return true;
        return false;
    }
}
