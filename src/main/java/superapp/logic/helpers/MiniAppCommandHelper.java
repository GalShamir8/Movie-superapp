package superapp.logic.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import superapp.common.Helper;
import superapp.dal.IdGeneratorCrud;
import superapp.dal.UsersCrud;
import superapp.data.IdGeneratorEntity;
import superapp.data.MiniAppCommandEntity;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.models.*;
import superapp.requestModels.MiniAppCommandBoundaryModel;
import superapp.view.MiniAppCommandBoundary;
import superapp.view.ObjectBoundary;


import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static superapp.common.Constants.USER_NOT_FOUND_MSG;

@Component
public class MiniAppCommandHelper {

    private String superAppName;
    private ObjectMapper jacksonMapper;

    private final IdGeneratorCrud idGenerator;

    private final UsersCrud usersCrud;


    @Autowired
    public MiniAppCommandHelper(@Value("${spring.application.name}")String superAppName,
                                IdGeneratorCrud idGenerator, UsersCrud usersCrud){
        this.superAppName = superAppName;
        jacksonMapper = new ObjectMapper();
        this.idGenerator = idGenerator;
        this.usersCrud = usersCrud;
    }
    public MiniAppCommandBoundary buildBoundaryFromEntity(MiniAppCommandEntity miniAppCommandEntity) {
        return new MiniAppCommandBoundary(getCommandId(miniAppCommandEntity),
                miniAppCommandEntity.getCommand(),
                getTargetObject(miniAppCommandEntity),
                miniAppCommandEntity.getInvocationTimestamp(),
                getInvokedBy(miniAppCommandEntity),
                buildCommandAttributesFromStr(miniAppCommandEntity.getCommandAttributes()));
    }

    private Map<String, Object> buildCommandAttributesFromStr(String commandAttributes) {
        return (Map<String, Object>) Helper.jsonToMap(jacksonMapper, commandAttributes);
    }

    private String buildCommandAttributesStr(Map<String, Object> commandAttributes) {
        return Helper.mapToJson(jacksonMapper, commandAttributes);
    }

    public String generateCommandId(String superAppName, String miniAppName, String internalCommandId){
       return superAppName + "_" + miniAppName + "_" + internalCommandId;
    }

    //todo crush when run because sometimes no ' ' and str[1] out of bounds
    private InvokedBy getInvokedBy(MiniAppCommandEntity miniAppCommandEntity) {
        InvokedBy invokedBy = new InvokedBy();
        String str = miniAppCommandEntity.getInvokedById().replace("2023.ohad.saada","");
        invokedBy.setUserId(new UserId("2023.ohad.saada",str));
        return invokedBy;
    }

    private TargetObject getTargetObject(MiniAppCommandEntity miniAppCommandEntity) {
        TargetObject targetObject = new TargetObject();
        ObjectId oId = new ObjectId();
        oId.setInternalObjectId(miniAppCommandEntity.getTargetObjectId());
        targetObject.setObjectId(oId);
        return targetObject;
    }

    private CommandId getCommandId(MiniAppCommandEntity miniAppCommandEntity) {
        CommandId cmId = new CommandId();
        String[] values = miniAppCommandEntity.getId().split("_");
        return new CommandId(values[0], values[1], values[2]);
    }

    public MiniAppCommandEntity buildEntityFromBoundary(String miniAppName, MiniAppCommandBoundaryModel command) throws JsonProcessingException {
        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        this.idGenerator.delete(helper);
        return new MiniAppCommandEntity(
                generateCommandId(superAppName,
                        miniAppName,
                        "" + helper.getId()
                ),
                miniAppName,
                command.getTargetObject().getObjectId().getInternalObjectId(),
                new Date(),
                command.getInvokedBy().getUserId().getEmail(),
                buildCommandAttributesStr(command.getCommandAttributes()));
    }

    public ObjectBoundary buildsuperAppBoundaryFromEntity(SuperAppObjectEntity superAppObjectEntity) {
        return new ObjectBoundary(
                extractObjectId(superAppObjectEntity.getObjectId()),
                superAppObjectEntity.getType(),
                superAppObjectEntity.getAlias(),
                superAppObjectEntity.isActive(),
                superAppObjectEntity.getCreatedAt(),
                getUserId(superAppObjectEntity.getUserId()),
                (Map<String, Object>) Helper.jsonToMap(jacksonMapper, superAppObjectEntity.getDetails())
        );
    }

    private UserId getUserId(String userId) {
        UserEntity userEntity = getUser(userId);
        return new UserId(superAppName, userEntity.getEmail());
    }

    public UserEntity getUser(String userId) {
        Optional<UserEntity> userEntity = usersCrud.findById(userId);
        if (userEntity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG);
        return userEntity.get();
    }

    private ObjectId extractObjectId(String objectId) {
        String[] split = objectId.split("_");
        if (split.length < 2)
            return new ObjectId();

        String superapp = split[0];
        String id = split[1];
        return new ObjectId(superapp, id);
    }
}
