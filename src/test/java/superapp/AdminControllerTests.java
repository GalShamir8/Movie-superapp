package superapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import superapp.common.Helper;
import superapp.fixture.Generator;
import superapp.logic.helpers.UserHelper;
import superapp.models.CommandId;
import superapp.models.InvokedBy;
import superapp.models.TargetObject;
import superapp.models.UserId;
import superapp.requestModels.ObjectBoundaryModel;
import superapp.requestModels.UserBoundaryModel;
import superapp.view.MiniAppCommandBoundary;
import superapp.view.ObjectBoundary;
import superapp.view.UserBoundary;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerTests {
    private int port;
    private RestTemplate restTemplate;
    private String endpointUrl;
    private String miniappUrl;
    private final static String BASE_URL =  "http://localhost";
    private UserBoundary adminUser,superAppUser;
    private boolean isAdminUserDeleted = false;
    private UserHelper userHelper;

    @LocalServerPort
    public void setPort(int port){
        this.port = port;
    }

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.userHelper = new UserHelper("2023.ohad.saada");
        this.endpointUrl = BASE_URL + ":" + this.port + "/superapp/admin";
        this.miniappUrl = endpointUrl +"/miniapp";
    }
    @BeforeEach
    public void createUsers(){
        String userUrl = BASE_URL + ":"  + this.port + "/superapp/users";
        adminUser = this.restTemplate.postForObject(userUrl, MockCreator.buildMockAdminRequestModel(), UserBoundary.class);
        superAppUser = this.restTemplate.postForObject(userUrl, MockCreator.buildMockSuperappUserModel(),UserBoundary.class);
        isAdminUserDeleted = false;
    }
    @AfterEach
    public void cleanDatabase() {
        if(!isAdminUserDeleted) {
            String deleteUsersURL = this.endpointUrl + "/users?userSuperapp=2023.ohad.saada&userEmail=admin@test.com";
            String deleteCommandsURL = this.endpointUrl + "/miniapp?userSuperapp=2023.ohad.saada&userEmail=admin@test.com";
            this.restTemplate
                    .delete(deleteCommandsURL);
            this.restTemplate
                    .delete(deleteUsersURL);
            isAdminUserDeleted = true;
        }
    }

    @Test
    public void deleteAllUsersTests(){
        String userUrl = BASE_URL + ":"  + this.port + "/superapp/users";
        IntStream.range(0, 5)
                .mapToObj(index-> MockCreator.buildMockUserRequestModel())
                .forEach(userModel-> restTemplate.postForObject(userUrl, userModel, UserBoundary.class));

        this.restTemplate
                .delete(endpointUrl + "/users?userSuperapp=2023.ohad.saada&userEmail=admin@test.com");

        this.isAdminUserDeleted = true;
        assertThat(this.restTemplate.getForObject(userUrl, UserBoundary[].class)).hasSize(0);
    }

    @Test
    public void deleteAllObjectsTests(){
        String objectUrl = BASE_URL + ":" + this.port + "/superapp/objects";
        IntStream.range(0, 5)
                .mapToObj(index-> MockCreator.buildMockSuperAppObjectRequestModel())
                .forEach(objectModel-> restTemplate.postForObject(objectUrl, objectModel, ObjectBoundary.class));

        this.restTemplate
                .delete(endpointUrl + "/objects?userSuperapp=2023.ohad.saada&userEmail=admin@test.com");

        assertThat(this.restTemplate.getForObject(objectUrl+"?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com&page=0&size=5", ObjectBoundary[].class)).hasSize(0);
    }

    @Test
    public void deleteAllCommandsHistoryTests(){
        String getCommandsURL = BASE_URL + ":" + this.port + "/superapp/miniapp";
         IntStream // Posting (adding) 5 miniapps
                .range(0, 5)
                .mapToObj(index-> MockCreator.buildMockMiniAppCommandBoundary())
                .forEach(miniAppModel-> restTemplate.getForObject(endpointUrl+"/init?userSuperapp=2023.ohad.saada&userEmail=admin@test.com", MiniAppCommandBoundary.class));

        restTemplate.delete(miniappUrl + "?userSuperapp=2023.ohad.saada&userEmail=admin@test.com");

        MiniAppCommandBoundary [] shouldBeEmptyList = restTemplate.getForObject(getCommandsURL,MiniAppCommandBoundary[].class);

        assertThat(shouldBeEmptyList).hasSize(0);

    }

    @Test
    public void exportAllUsers(){
        String userUrl = BASE_URL +":" + this.port + "/superapp/users";
        List<UserBoundary> userBoundaries = IntStream.range(0, 5)
                .mapToObj(index-> MockCreator.buildMockUserRequestModel())
                .map(userModel-> restTemplate.postForObject(userUrl, userModel, UserBoundary.class))
                .collect(Collectors.toList());
        userBoundaries.add(adminUser);
        userBoundaries.add(superAppUser);

        UserBoundary userBoundary[] = restTemplate
                .getForObject(endpointUrl + "/users?userSuperapp=2023.ohad.saada&userEmail=admin@test.com",UserBoundary[].class);

        assertThat(userBoundary)
                .hasSize(7) //expected 5 + admin is 6 superappuser 7
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(userBoundaries);    }

    //TODO: fix later
    @Test
    public void testGetAllMiniApps(){ //TODO: Fix getForObject to return more than 1 MiniAppCommandBoundary
        List<MiniAppCommandBoundary> miniApps = IntStream // Posting (adding) 5 miniapps
                .range(0, 1)
                .mapToObj(index-> new MiniAppCommandBoundary())
                .map(miniAppModel-> restTemplate.getForObject(endpointUrl+"/init?userSuperapp=2023.ohad.saada&userEmail=admin@test.com&size=5", MiniAppCommandBoundary.class))
                .collect(Collectors.toList());
        List<MiniAppCommandBoundary> actualObjects = Arrays.stream(this.restTemplate.getForObject(miniappUrl+"?userSuperapp=2023.ohad.saada&userEmail=admin@test.com", MiniAppCommandBoundary[].class)).toList();
        System.out.println(miniApps.size());
        System.out.println("ActualObjectSize" + actualObjects.size());
        assertThat(actualObjects).asList().hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(miniApps);
    }
    //TODO: Fix later
    @Test
    public void testGetCommandsForMiniApp(){ //TODO: Fix getForObject to return more than 1 MiniAppCommandBoundary
        MiniAppCommandBoundary specificMiniApp = MockCreator.buildMockMiniAppCommandBoundary();
        String invokeUrl = BASE_URL + ":" + this.port + "/superapp/miniapp";
        String specificCommandUrl = miniappUrl + specificMiniApp.getCommand();
        restTemplate.postForObject(invokeUrl+specificMiniApp.getCommand(), specificMiniApp, MiniAppCommandBoundary.class);
        MiniAppCommandBoundary[] actualObject = this.restTemplate.getForObject(specificCommandUrl, MiniAppCommandBoundary[].class);


        assertThat(actualObject[0]).isEqualTo(specificCommandUrl);
    }

}

