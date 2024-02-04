package superapp.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import superapp.logic.UsersService;
import superapp.models.UserId;
import superapp.requestModels.UserBoundaryModel;
import superapp.view.UserBoundary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static superapp.common.Constants.*;
import static superapp.common.Helper.getSuperAppDomain;


@RestController
public class UsersController {
    private String superAppName;
    private final static String CREATE_USER_URL = BASE_URL + "/users";
    private final static String LOGIN_USER_URL = BASE_URL + "/users" + "/{superapp}" + "/{email}";
    private final static String UPDATE_USER_URL = BASE_URL + "/users" + "/{superapp}" + "/{userEmail}";
    private final static String INDEX_URL = BASE_URL + "/users";
    private final static String DELETE_ALL_URL = BASE_URL + "/users";
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService, @Value("${spring.application.name}") String superAppName) {
        this.usersService = usersService;
        this.superAppName = superAppName;
    }

    @RequestMapping(
            path = CREATE_USER_URL,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserBoundary create(@RequestBody UserBoundaryModel user) throws JsonProcessingException {
        return usersService.createUser(new UserBoundary(
                user.getRole(),
                user.getAvatar(),
                user.getUsername(),
                new UserId(getSuperAppDomain(), user.getEmail()),
                new Date()
                )
        );
    }

    @RequestMapping(
            path = LOGIN_USER_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserBoundary login(@PathVariable("superapp") String superapp, @PathVariable("email") String email){
        return usersService.login(superapp, email);
    }

    @RequestMapping(
            path = UPDATE_USER_URL,
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public UserBoundary update(@PathVariable("superapp") String superapp,
                       @PathVariable("userEmail") String userEmail,
                       @RequestBody UserBoundaryModel user){

        UserBoundary userBoundary = new UserBoundary(
                user.getRole(),
                user.getAvatar(),
                user.getUsername(),
                new UserId(getSuperAppDomain(), user.getEmail()),
                null
        );

        return usersService.updateUser(superapp, userEmail, userBoundary);
    }

    @RequestMapping(
            path = INDEX_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserBoundary> index(){
        return usersService.getAllUsers();
    }

    @RequestMapping(
            path = DELETE_ALL_URL,
            method = RequestMethod.DELETE
    )
    public void deleteAll(){
        usersService.deleteAllUsers();
    }

}



