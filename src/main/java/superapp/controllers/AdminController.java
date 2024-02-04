package superapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import superapp.fixture.Generator;
import superapp.logic.AdminService;
import superapp.logic.UsersService;
import superapp.models.*;
import superapp.view.MiniAppCommandBoundary;
import superapp.view.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static superapp.common.Constants.*;

@RestController
public class AdminController {
    private final static String ADMIN_BASE_URL = BASE_URL + "/admin";
    private final static String DELETE_ALL_USERS = ADMIN_BASE_URL + "/users";
    private final static String DELETE_ALL_OBJECTS = ADMIN_BASE_URL + "/objects";
    private final static String DELETE_ALL_COMMANDS_HISTORY = ADMIN_BASE_URL + "/miniapp";
    private final static String EXPORT_ALL_USERS = ADMIN_BASE_URL + "/users";
    private final static String EXPORT_ALL_MINIAPP_COMMANDS = ADMIN_BASE_URL + "/miniapp";
    private final static String EXPORT_COMMANDS_HISTORY = ADMIN_BASE_URL + "/miniapp/{miniAppName}";
    private final static String UPDATE_DATABASE = ADMIN_BASE_URL + "/update";
    private final static String INIT_COMMANDS = ADMIN_BASE_URL + "/init";


    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService, @Value("${spring.application.name}") String superAppName) {
        this.adminService = adminService;
    }

    @RequestMapping(
            path = DELETE_ALL_USERS,
            method = RequestMethod.DELETE
    )
    public void deleteAllUsers(@RequestParam(name = "userSuperapp") String superapp,
                               @RequestParam(name = "userEmail") String email) {
        adminService.deleteAllUsers(superapp, email);
    }

    @RequestMapping(
            path = DELETE_ALL_OBJECTS,
            method = RequestMethod.DELETE
    )
    public void deleteAllObjects(@RequestParam(name = "userSuperapp") String superapp,
                                 @RequestParam(name = "userEmail") String email) {
        adminService.deleteAllObjects(superapp, email);
    }

    @RequestMapping(
            path = DELETE_ALL_COMMANDS_HISTORY,
            method = RequestMethod.DELETE
    )
    public void deleteAllCommandsHistory(@RequestParam(name = "userSuperapp") String superapp,
                                         @RequestParam(name = "userEmail") String email) {
        adminService.deleteAllCommandsHistory(superapp, email);
    }

    @RequestMapping(
            path = EXPORT_ALL_USERS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserBoundary> exportAllUsers(@RequestParam(name = "userSuperapp") String superapp,
                                             @RequestParam(name = "userEmail") String email,
                                             @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                             @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return adminService.exportAllUsers(superapp, email, size, page);
    }

    @RequestMapping(
            path = EXPORT_ALL_MINIAPP_COMMANDS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MiniAppCommandBoundary> exportAllMiniAppCommandsHistory(@RequestParam(name = "userSuperapp") String superapp,
                                                                        @RequestParam(name = "userEmail") String email,
                                                                        @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                                                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return adminService.exportAllCommandsHistory(superapp, email, size, page);
    }

    @RequestMapping(
            path = EXPORT_COMMANDS_HISTORY,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MiniAppCommandBoundary> exportSpecificMiniAppCommandsHistory(@PathVariable("miniAppName") String miniAppName,
                                                                             @RequestParam(name = "userSuperapp", defaultValue = "", required = false) String superapp,
                                                                             @RequestParam(name = "userEmail", defaultValue = "amit.maimon@s.afeka.ac.il", required = false) String email,
                                                                             @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                                                             @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return adminService.exportSpecificCommandsHistory(superapp, email, miniAppName, size, page);
    }


//    @RequestMapping(
//            path = INIT_COMMANDS,
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public MiniAppCommandBoundary init(@RequestParam(name = "userSuperapp") String superapp,
//                     @RequestParam(name = "userEmail") String email)  throws JsonProcessingException {
//
//
//        return adminService.initCommand(superapp ,email);
//    }

    @RequestMapping(
            path = UPDATE_DATABASE,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String updateData(
            @RequestParam(name = "userSuperapp", defaultValue = "", required = false) String superapp,
            @RequestParam(name = "userEmail", defaultValue = "amit.maimon@s.afeka.ac.il", required = false) String email) throws JsonProcessingException {

        return adminService.updateDataBase(superapp, email);
    }


}



