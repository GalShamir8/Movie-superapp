package superapp.models;

import static superapp.common.Helper.getSuperAppDomain;

public class CommandId {
    private String superApp;
    private String miniApp;
    private String internalCommandId;

    public CommandId(String superApp, String miniApp, String internalCommandId) {
        this.superApp = superApp;
        this.miniApp = miniApp;
        this.internalCommandId = internalCommandId;
    }

    public CommandId() {
    }

    public String getSuperApp() {
        return superApp;
    }

    public void setSuperApp(String superApp) {
        this.superApp = superApp;
    }

    public String getMiniApp() {
        return miniApp;
    }

    public void setMiniApp(String miniApp) {
        this.miniApp = miniApp;
    }

    public void setId(String internalCommandId) {
        this.internalCommandId = internalCommandId;
    }

    public String getInternalCommandId() {
        return internalCommandId;
    }
}
