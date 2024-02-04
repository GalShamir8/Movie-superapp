package superapp.logic.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.myMovies.models.MediaGroup;
import superapp.myMovies.models.Movie;
import superapp.models.ObjectId;
import superapp.models.UserId;
import superapp.view.ObjectBoundary;

import java.util.Map;
import java.util.Optional;

import static superapp.common.Constants.USER_NOT_FOUND_MSG;

@Component
public class SuperAppObjectHelper {
    private final String superAppName;
    private final ObjectMapper jacksonMapper;
    private final IdGeneratorCrud idGenerator;
    private final UsersCrud usersCrud;

    @Autowired
    public SuperAppObjectHelper(@Value("${spring.application.name}") String superAppName, IdGeneratorCrud idGenerator,
                                UsersCrud usersCrud) {
        this.superAppName = superAppName;
        this.usersCrud = usersCrud;
        jacksonMapper = new ObjectMapper();
        this.idGenerator = idGenerator;
    }

    public SuperAppObjectEntity buildEntityFromBoundary(ObjectBoundary superAppObjectBoundary) {
        return new SuperAppObjectEntity(
                superAppObjectBoundary.getType(),
                generateObjectId(),
                superAppObjectBoundary.getAlias(),
                superAppObjectBoundary.isActive(),
                superAppObjectBoundary.getCreatedAt(),
                Helper.mapToJson(jacksonMapper, superAppObjectBoundary.getObjectDetails()),
                generateUserId(
                        superAppObjectBoundary.getCreatedBy().getSuperapp(),
                        superAppObjectBoundary.getCreatedBy().getEmail()
                )
        );
    }
    public String generateUserId(String superApp, String userEmail) {
        String idComb = superApp + "_" + userEmail;
        return Helper.generateUuid(idComb);
    }

    private String generateObjectId() {
        IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
        this.idGenerator.delete(helper);
        return getId(superAppName, "" + helper.getId());
    }

    public String getId(String superapp, String id) {
        return superAppName + "_" + id;
    }

    public MediaGroup buildMediaGroupFromMap(Map<String, Object> response) throws JsonProcessingException {
        String json = Helper.mapToJson(jacksonMapper, response);
        jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return jacksonMapper.readValue(json, MediaGroup.class);

    }

    public ObjectBoundary buildBoundaryFromEntity(SuperAppObjectEntity superAppObjectEntity) {
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

    public SuperAppObjectEntity updateEntity(SuperAppObjectEntity superAppObjectEntity, ObjectBoundary objBoundary) {
        superAppObjectEntity.setDetails(Helper.mapToJson(jacksonMapper, objBoundary.getObjectDetails()));
        superAppObjectEntity.setActive(objBoundary.isActive());
        superAppObjectEntity.setAlias(objBoundary.getAlias());
        superAppObjectEntity.setType(objBoundary.getType());
        return superAppObjectEntity;
    }


    public Map<String, Object> buildMapFromMovie(Movie movie) throws JsonProcessingException {
        return (Map<String, Object>) Helper.jsonToMap(jacksonMapper, jacksonMapper.writeValueAsString(movie));
    }
}
