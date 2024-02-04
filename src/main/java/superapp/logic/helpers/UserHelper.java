package superapp.logic.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import superapp.common.Helper;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.models.UserId;
import superapp.myMovies.models.UserMediaLists;
import superapp.view.UserBoundary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static superapp.common.Constants.*;

import java.util.Date;
import java.util.Map;


@Component
public class UserHelper {

    private String superAppName;
    private ObjectMapper mapper;

    @Autowired
    public UserHelper(@Value("${spring.application.name}") String superAppName) {
        mapper = new ObjectMapper();
        this.superAppName = superAppName;
    }

    public UserEntity buildEntityFromBoundary(UserBoundary userBoundary) throws JsonProcessingException {
        String userEmail = userBoundary.getUserId().getEmail();
        if (!Helper.validateEmail(userEmail)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, INVALID_EMAIL_MSG);
        }
        return new UserEntity(generateUserId(superAppName, userEmail),
                userBoundary.getUsername(),
                userBoundary.getAvatar(),
                UserRole.valueOf(userBoundary.getRole()),
                userEmail,
                new Date()
        );
    }

    public String generateUserId(String superApp, String userEmail) {
        String idComb = superApp + "_" + userEmail;
        return Helper.generateUuid(idComb);
    }

    public UserBoundary buildBoundaryFromEntity(UserEntity userEntity) {
        return new UserBoundary(userEntity.getRole().name(),
                userEntity.getAvatar(),
                userEntity.getUsername(),
                getUserId(userEntity.getEmail()),
                userEntity.getCreatedAt()
        );
    }

    private UserId getUserId(String userEmail) {
        return new UserId(superAppName, userEmail);
    }

}
