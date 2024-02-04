package superapp.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.server.ResponseStatusException;
import superapp.view.MiniAppCommandBoundary;
import superapp.view.UserBoundary;

import java.util.List;

public interface AdminService{
    /**
     * Delete all the users in the system
     */
    void deleteAllUsers(String userSuperApp, String userEmail);


    /**
     * Delete all the objects in the system
     */
    void deleteAllObjects(String userSuperApp, String userEmail);

    /**
     * Delete history
     */
    void deleteAllCommandsHistory(String userSuperApp, String userEmail);

    /**
     * index
     * @return list of all the users in the system
     */
    List<UserBoundary> exportAllUsers(String userSuperApp, String userEmail, int size, int page);

    /**
     Get all commands from all the mini apps
     @return a List of all MiniAppCommandBoundary - mini apps commands
     **/
    List<MiniAppCommandBoundary> exportAllCommandsHistory(String userSuperApp, String userEmail, int size, int page);


    /**
     * Search for a specific command
     * @return a List of MiniAppCommandBoundary from specific app
     * @throws ResponseStatusException in cases the requested command is not found (404)
     */
    List<MiniAppCommandBoundary> exportSpecificCommandsHistory(String userSuperApp, String userEmail,String miniAppName, int size, int page) throws ResponseStatusException;




    String updateDataBase(String userSuperApp, String userEmail)throws JsonProcessingException ;

//    MiniAppCommandBoundary initCommand(String userSuperApp, String userEmail) throws JsonProcessingException;



}
