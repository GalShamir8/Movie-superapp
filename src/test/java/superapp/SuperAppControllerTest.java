package superapp;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import superapp.fixture.Generator;
import superapp.models.ObjectId;
import superapp.requestModels.ObjectBoundaryModel;
import superapp.requestModels.UserBoundaryModel;
import superapp.view.ObjectBoundary;
import superapp.view.UserBoundary;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuperAppControllerTest {
    private int port;
    private RestTemplate restTemplate;
    private String endpointUrl,adminEndpointUrl;

    private String superAppName = "2023.ohad.saada";
    private final static String BASE_URL = "http://localhost";
    private UserBoundary superappUser,adminUser;

    @LocalServerPort
    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void init() {
        String userUrl = BASE_URL + ":"  + this.port + "/superapp/users";
        this.restTemplate = new RestTemplate();
        this.endpointUrl = String.format(Locale.US, "%s:%d/%s/%s", BASE_URL, port, "superapp", "objects");
        adminEndpointUrl = BASE_URL + ":" + port + "/superapp/admin";
        this.restTemplate.postForObject(userUrl, MockCreator.buildMockSuperappUserModel(),UserBoundary.class);
    }
    @BeforeEach
    public void createUser(){
        String userUrl = BASE_URL + ":"  + this.port + "/superapp/users";
        adminUser = this.restTemplate.postForObject(userUrl, MockCreator.buildMockAdminRequestModel(), UserBoundary.class);
        superappUser = this.restTemplate.postForObject(userUrl, MockCreator.buildMockSuperappUserModel(),UserBoundary.class);
    }

    @AfterEach
    public void cleanDatabase() {
        this.restTemplate
                .delete(adminEndpointUrl + "/objects?userSuperapp=2023.ohad.saada&userEmail=admin@test.com");
    }

    @Test
    public void testCreate() throws Exception {
        ObjectBoundaryModel objectModel = MockCreator.buildMockSuperAppObjectRequestModel();
        String getAllURL = endpointUrl+"?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com&page=0&size=10";
        ObjectBoundary whatWasCreated = restTemplate.postForObject(endpointUrl, objectModel, ObjectBoundary.class);
        ObjectBoundary[] actualObject = restTemplate.getForObject(getAllURL , ObjectBoundary[].class);


        assert (whatWasCreated.getObjectId().equals(actualObject[0].getObjectId()));
    }

    @Test
    public void testUpdate() throws Exception{
        ObjectBoundaryModel objectModel = MockCreator.buildMockSuperAppObjectRequestModel();
        ObjectBoundary postedObject = restTemplate.postForObject(endpointUrl, objectModel, ObjectBoundary.class);
        postedObject.setType("textType");
        String updateUrl = endpointUrl+"/"+superAppName+"/"+postedObject.getObjectId().getInternalObjectId()+
                "?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com";
        String getObjectUrl = endpointUrl+"/"+superAppName+"/"+postedObject.getObjectId().getInternalObjectId()+
                "?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com";
        restTemplate.put(updateUrl, postedObject, ObjectBoundary.class);
        String expected = "textType";
        ObjectBoundary updatedObject = restTemplate.getForObject(getObjectUrl,ObjectBoundary.class);
        String updatedType = updatedObject.getType();
        System.out.println("The returned Type: " +updatedType);
        System.out.println("The Inserted Type: " +postedObject.getType());
        AssertionsForClassTypes.assertThat(updatedType).isEqualTo(expected);
    }

    @Test
    public void getAllSuperAppObjects() throws Exception{
        String getAllURL = endpointUrl+"?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com&page=0&size=10";
        List<ObjectBoundary> inDb = IntStream
                .range(0, 5)
                .mapToObj(index -> MockCreator.buildMockSuperAppObjectRequestModel())
                .map(objectModel-> restTemplate.postForObject(endpointUrl, objectModel, ObjectBoundary.class))
                .collect(Collectors.toList());
        ObjectBoundary[] actualObjects = this.restTemplate.getForObject(getAllURL, ObjectBoundary[].class);
        AssertionsForClassTypes.assertThat(actualObjects)
                .hasSize(5)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(inDb);
    }
    @Test
    public void getSpecificSuperAppObject() throws Exception{
        ObjectBoundaryModel objectModel = MockCreator.buildMockSuperAppObjectRequestModel();
        ObjectBoundary postedObject = restTemplate.postForObject(endpointUrl, objectModel, ObjectBoundary.class);
        String getObjectUrl = endpointUrl+"/"+superAppName+"/"+postedObject.getObjectId().getInternalObjectId()+
                "?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com";

        ObjectBoundary actualObject = this.restTemplate.getForObject(getObjectUrl, ObjectBoundary.class);
        AssertionsForClassTypes.assertThat(postedObject.getObjectId().getInternalObjectId()).isEqualTo(actualObject.getObjectId().getInternalObjectId());
    }
    @Test
    public void BindChildObjectsAndGetChildrenObjectsOfParentWithPaginationTest() throws Exception{
        ObjectBoundaryModel childObjectModel = MockCreator.buildMockSuperAppObjectRequestModel();
        ObjectBoundaryModel childObjectModel2 = MockCreator.buildMockSuperAppObjectRequestModel();
        ObjectBoundaryModel parentObjectModel = MockCreator.buildMockSuperAppObjectRequestModel();
        ObjectBoundary childObject = restTemplate.postForObject(endpointUrl, childObjectModel, ObjectBoundary.class);
        ObjectBoundary childObject2 = restTemplate.postForObject(endpointUrl, childObjectModel2, ObjectBoundary.class);
        ObjectBoundary parentObject = restTemplate.postForObject(endpointUrl, parentObjectModel, ObjectBoundary.class);
        String bindChildrenUrl = endpointUrl+"/"+superAppName+"/"+parentObject.getObjectId().getInternalObjectId()+
                "/children?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com";

        List<ObjectBoundary> postedChildren = new ArrayList<ObjectBoundary>();
        postedChildren.add(childObject);postedChildren.add(childObject2);
        restTemplate.put(bindChildrenUrl,childObject.getObjectId(), ObjectId.class);
        restTemplate.put(bindChildrenUrl,childObject2.getObjectId(), ObjectId.class);
        String getChildrenUrl = bindChildrenUrl + "&size=0&page=10";
        ObjectBoundary[] childrenObjects = restTemplate.getForObject(getChildrenUrl,ObjectBoundary[].class);
        AssertionsForClassTypes.assertThat(childrenObjects).hasSize(2).usingRecursiveFieldByFieldElementComparator().isSubsetOf(postedChildren);
    }

    @Test
    public void BindChildObjectToParentAndGetAllParentsOfObjectTest() throws Exception{
        ObjectBoundaryModel childObjectModel = MockCreator.buildMockSuperAppObjectRequestModel();
        ObjectBoundaryModel parentObjectModel = MockCreator.buildMockSuperAppObjectRequestModel();
        ObjectBoundary childObject = restTemplate.postForObject(endpointUrl, childObjectModel, ObjectBoundary.class);
        ObjectBoundary parentObject = restTemplate.postForObject(endpointUrl, parentObjectModel, ObjectBoundary.class);
        String bindChildrenUrl = endpointUrl+"/"+superAppName+"/"+parentObject.getObjectId().getInternalObjectId()+
                "/children?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com";
        String getObjectParentsUrl = endpointUrl+"/"+superAppName+"/"+childObject.getObjectId().getInternalObjectId()+
                "/parents?userSuperapp=2023.ohad.saada&userEmail=superappUser@test.com&size=0&page=10";
        restTemplate.put(bindChildrenUrl,childObject.getObjectId(), ObjectId.class);
        ObjectBoundary actualParentObject = restTemplate.getForObject(getObjectParentsUrl,ObjectBoundary[].class)[0];

        AssertionsForClassTypes.assertThat(parentObject.getObjectId().getInternalObjectId()).isEqualTo(actualParentObject.getObjectId().getInternalObjectId());
    }

}
















