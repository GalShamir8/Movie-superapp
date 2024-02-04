package superapp.data;


import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import superapp.common.Helper;

import javax.persistence.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="MINIAPPCOMMANDS")
public class MiniAppCommandEntity {
    private String command;
    private String targetObjectId;
    private Date invocationTimestamp;
    private String invokedById;
    private String id;
    private String commandAttributes;


    public MiniAppCommandEntity() {}


    public MiniAppCommandEntity(String id,String command, String targetObjectId
            , Date invocationTimestamp, String invokedById, String commandAttributes) throws JsonProcessingException {
        this.command = command;
        this.targetObjectId = targetObjectId;
        this.invocationTimestamp = invocationTimestamp;
        this.invokedById = invokedById;
        this.id = id;
        this.commandAttributes = commandAttributes;

    }
    @Id
    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setIid(String id){
        this.id = id;
    }
    @Column(name = "Command_Name")
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    @Column(name = "InvokedById")
    public String getInvokedById() {
        return invokedById;
    }

    public void setInvokedById(String invokedById) {
        this.invokedById = invokedById;
    }

    public String getTargetObjectId() {
        return targetObjectId;
    }

    public void setTargetObjectId(String targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }
    @Lob
    @Column(name = "COMMAND_ATTRIBUTES")
    public String getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(String commandAttributes) {
        this.commandAttributes = commandAttributes;
    }


}
