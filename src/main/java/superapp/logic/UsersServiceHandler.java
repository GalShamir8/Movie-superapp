package superapp.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import superapp.common.Helper;
import superapp.dal.UsersCrud;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.logic.helpers.UserHelper;
import superapp.view.UserBoundary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.StreamSupport;

import static superapp.common.Constants.USER_NOT_FOUND_MSG;

@Service
public class UsersServiceHandler implements UsersService {
//    private Map<String, UserEntity> users;
    protected UsersCrud users;
    protected final UserHelper userHelper;
    private Log logger = LogFactory.getLog(UsersServiceHandler.class);
    private ObjectMapper mapper ;

    @Autowired
    public UsersServiceHandler(UserHelper userHelper, UsersCrud users) {
        mapper = new ObjectMapper();
        this.userHelper = userHelper;
        this.users = users;
    }

    @Transactional
    @Override
    public UserBoundary createUser(UserBoundary user) throws JsonProcessingException {
        logger.debug("userServiceHandler createUser is about to be executed");
        UserEntity userEntity = userHelper.buildEntityFromBoundary(user);
        logger.debug("userServiceHandler createUser user has been built from boundary");
        saveUser(userEntity);
        return userHelper.buildBoundaryFromEntity(userEntity);
    }

    private void saveUser(UserEntity userEntity) {
        logger.debug("userServiceHandler saveUser is about to be executed");
        users.save(userEntity);
        logger.trace("userServiceHandler saveUser - User Saved to Database: "+userEntity);
    }

    @Transactional(readOnly=true)
    @Override
    public UserBoundary login(String userSuperApp, String userEmail) throws ResponseStatusException {
        logger.debug("userServiceHandler login is about to be executed");
        Optional<UserEntity> userEntity = users.findById(userHelper.generateUserId(userSuperApp, userEmail));
        if (userEntity.isEmpty()) {
            logger.warn(USER_NOT_FOUND_MSG +" User name: "+userSuperApp+" Email: "+userEmail);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG);
        }
        logger.trace("userServiceHandler login - User has logged into the system");
        return userHelper.buildBoundaryFromEntity(userEntity.get());
    }

    @Transactional
    @Override
    public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary user) throws ResponseStatusException {
        logger.debug("userServiceHandler updateUser is about to be executed");
        Optional<UserEntity> optionalUserEntity = users.findById(userHelper.generateUserId(userSuperApp, userEmail));
        if (optionalUserEntity.isEmpty()) {
            logger.warn(USER_NOT_FOUND_MSG +" User name: "+userSuperApp+" Email: "+userEmail);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG);
        }
        UserEntity userEntity = optionalUserEntity.get();
        UserEntity updateUserEntity = new UserEntity(
                userEntity.getId(),
                user.getUsername(),
                user.getAvatar(),
                UserRole.valueOf(user.getRole()),
                userEntity.getEmail(), // email update not allowed
                userEntity.getCreatedAt()
                );

        saveUser(updateUserEntity);
        logger.trace("userServiceHandler updateUser - User has been updated");
        return userHelper.buildBoundaryFromEntity(updateUserEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserBoundary> getAllUsers() {
        logger.debug("userServiceHandler getAllUsers is about to be executed");
        logger.trace("userServiceHandler getAllUsers - Getting all users...");
        return StreamSupport.stream(this.users
                                .findAll()
                                .spliterator(), false)
                .map(userHelper::buildBoundaryFromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void deleteAllUsers() {
        logger.debug("userServiceHandler deleteAllUsers is about to be executed");
        for (UserEntity user: users.findAll()){
            user.deleteAllLFavorites();
            user.deleteAllLWishlist();
        }
        this.users.deleteAll();
        logger.trace("userServiceHandler deleteAllUsers - All users have been deleted!");
    }

}
