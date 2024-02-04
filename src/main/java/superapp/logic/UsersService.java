package superapp.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import superapp.view.UserBoundary;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface UsersService {
    /**
     * Create new UserEntity in the storage
     * @param user  - UserBoundary representative with relevant attributes
     * @return new UserBoundary
     */
    UserBoundary createUser(UserBoundary user) throws JsonProcessingException;

    /**
     * Login user to the system - with corresponding email, super app
     * @param userSuperApp - user super app string representative
     * @param userEmail
     * @return UserBoundary for user with given email, superApp in the storage
     * @throws ResponseStatusException http_status.NOT_FOUND (404) - if the user doesn't exist in the system
     */
    UserBoundary login(String userSuperApp, String userEmail) throws ResponseStatusException;

    /**
     * Update user in the system - with corresponding email, super app
     * @param userSuperApp - user super app string representative
     * @param userEmail
     * @return updated UserBoundary align with new attributes
     * @throws ResponseStatusException http_status.NOT_FOUND (404) - if the user doesn't exist in the system
     */
    UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary user) throws ResponseStatusException;

    /**
     * index
     * @return list of all the users in the system
     */
    List<UserBoundary> getAllUsers();

    /**
     * Delete all the users in the system
     */
    void deleteAllUsers();
}
