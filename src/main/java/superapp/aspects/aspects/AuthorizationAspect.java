package superapp.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import superapp.dal.UsersCrud;
import superapp.logic.AdminServiceHandler;
import superapp.logic.helpers.UserHelper;

import static superapp.common.Constants.USER_NOT_AUTHORISED;
import static superapp.common.Constants.USER_NOT_FOUND_MSG;

@Aspect
@Component
public class AuthorizationAspect {

    private final UserHelper userHelper;
    private final UsersCrud users;

    @Autowired
    public AuthorizationAspect(UserHelper userHelper, UsersCrud users) {
        this.userHelper = userHelper;
        this.users = users;
    }

    @Before(value="@annotation(RequiresAuthorization)")
    public void authorize(JoinPoint joinPoint) throws Throwable {
        // Extract values from method parameters
        System.out.println("[DEBUG]: in authorize");
        Object[] args = joinPoint.getArgs();
        System.out.println("[DEBUG]: arguments = " + Arrays.toString(args));
//        ArrayList<String> parameterNames = (ArrayList<String>) Arrays
//                        .stream(((MethodSignature) joinPoint
//                        .getSignature())
//                        .getParameterNames()).toList();
        List<String> parameterNames = Arrays.stream(((MethodSignature) joinPoint.getSignature()).getParameterNames()).toList();
        String userSuperApp = (String) args[parameterNames.indexOf("userSuperapp")];
        String userEmail = (String) args[parameterNames.indexOf("email")];

        System.out.println("[DEBUG]: userSuperapp =" + userSuperApp + " userEmail =" + userEmail);
        makeAuthorizationChecks(userSuperApp, userEmail);
    }

    public void makeAuthorizationChecks(String userSuperApp, String userEmail) {
        System.out.println("[DEBUG]: in makeAuthorizationChecks");
        String id = userHelper.generateUserId(userSuperApp, userEmail);
        Optional<UserEntity> user = users.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG);
        }
        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_NOT_AUTHORISED);
        }
    }
}



