package superapp.requestModels;

import superapp.models.CommandId;
import superapp.models.InvokedBy;
import superapp.models.TargetObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MiniAppCommandBoundaryModel {
    private String command;
    private TargetObject targetObject;
    private InvokedBy invokedBy;
    private Map<String, Object> commandAttributes;

    public MiniAppCommandBoundaryModel (String command, TargetObject targetObject,
                                        InvokedBy invokedBy, Map<String, Object> commandAttributes) {
        this.command = command;
        this.targetObject = targetObject;
        this.invokedBy = invokedBy;
        this.commandAttributes = commandAttributes;
    }

    public MiniAppCommandBoundaryModel() {
        commandAttributes = new HashMap<String, Object>();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public TargetObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(TargetObject targetObject) {
        this.targetObject = targetObject;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }

    @Override
    public String toString() {
        return "miniAppCommandBoundary{" +
                ", command='" + command + '\'' +
                ", targetObject=" + targetObject +
                ", invokedBy=" + invokedBy +
                ", commandAttributes=" + commandAttributes +
                '}';
    }
}
