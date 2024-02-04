package superapp.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.server.ResponseStatusException;
import superapp.requestModels.MiniAppCommandBoundaryModel;
import superapp.view.MiniAppCommandBoundary;

import java.util.List;


public interface MiniAppCommandsService {
    /**
    * Delete all commands currently on all of the mini apps
    * */
    void deleteAllCommands();
    /**
    Get all commands from all of the mini apps
    @return a List of all MiniAppCommandBoundary - mini apps commands
     **/
    List<MiniAppCommandBoundary> getAllCommands();

    /**
     * Search for a specific command and invoke it
     * @param command as MiniAppCommandBoundary  to search for
     * @return a generic object (contains the details of the invoked command)
     * @throws ResponseStatusException in cases the requested command is not found (404)
     */
    Object invokeCommand( String miniAppName, MiniAppCommandBoundaryModel command) throws Exception;

    /**
     * Get all commands from a certain miniapp
     * @param miniAppName as String,
     * @return a List of MiniAppCommandBoundary - list of commands from this certain mini app
     * @throws ResponseStatusException in the case of the requested miniapp is missing
     */
    List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) throws ResponseStatusException;

}
