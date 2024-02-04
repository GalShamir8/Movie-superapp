package superapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import superapp.logic.MiniAppCommandsService;
import superapp.requestModels.MiniAppCommandBoundaryModel;
import superapp.view.MiniAppCommandBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static superapp.common.Constants.BASE_URL;


@RestController
public class MiniAppController {
    private String superAppName;
    private final static String INVOKE_URL = BASE_URL + "/miniapp" + "/{miniAppName}";
    private final static String DELETE_ALL_URL = BASE_URL + "/miniapp";
    private final static String GET_ALL_COMMANDS_URL = BASE_URL + "/miniapp";
    private final static String GET_ALL_MINI_APP_COMMANDS_URL = BASE_URL + "/miniapp" + "/{miniAppName}";

    private final MiniAppCommandsService miniAppCommandService;

    @Autowired
    public MiniAppController(MiniAppCommandsService miniAppCommandService, @Value("${spring.application.name}") String superAppName) {
        this.miniAppCommandService = miniAppCommandService;
        this.superAppName = superAppName;
    }

    @RequestMapping(
            path = INVOKE_URL,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object invokeMiniAppCommands(@PathVariable("miniAppName") String miniAppName, @RequestBody MiniAppCommandBoundaryModel miniAppCommandBoundary) throws Exception {
        return miniAppCommandService.invokeCommand(miniAppName, miniAppCommandBoundary);
    }


    @RequestMapping(
            path = DELETE_ALL_URL,
            method = RequestMethod.DELETE
    )
    public void deleteAll() {
        miniAppCommandService.deleteAllCommands();
    }

    @RequestMapping(
            path = GET_ALL_COMMANDS_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MiniAppCommandBoundary> getAllCommands() {
        return miniAppCommandService.getAllCommands();
    }

    @RequestMapping(
            path = GET_ALL_MINI_APP_COMMANDS_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MiniAppCommandBoundary> getAllCommands(@PathVariable("miniAppName") String miniAppName) {
        return miniAppCommandService.getAllMiniAppCommands(miniAppName);
    }
}
