package superapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import superapp.fixture.Generator;
import superapp.myMovies.models.UserMediaLists;
import superapp.requestModels.UserBoundaryModel;
import superapp.view.UserBoundary;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersControllerTest {
    private int port;
    private RestTemplate restTemplate;
    private String endpointUrl;
    private String superAppName = "2023.ohad.saada";
    private final static String BASE_URL =  "http://localhost";

    @LocalServerPort
    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.endpointUrl = String.format(Locale.US, "%s:%d/%s/%s", BASE_URL, port, "superapp", "users");;
    }

    @AfterEach
    public void cleanDatabase() {
        this.restTemplate
                .delete(this.endpointUrl);
    }

    @Test
    public void testCreate() throws Exception{
        UserBoundaryModel userModel = MockCreator.buildMockUserRequestModel();
        String loginUrl = String.format(Locale.US, "%s/%s/%s", endpointUrl, superAppName, userModel.getEmail());

        UserBoundary expected = restTemplate.postForObject(endpointUrl, userModel, UserBoundary.class);
        UserBoundary actual = restTemplate.getForObject(loginUrl, UserBoundary.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void testUpdate() throws Exception{
        UserBoundaryModel userModel = MockCreator.buildMockUserRequestModel();
        String loginUrl = String.format(Locale.US, "%s/%s/%s", endpointUrl, superAppName, userModel.getEmail());
        String updateUrl = loginUrl;
        restTemplate.postForObject(endpointUrl, userModel, UserBoundary.class);
        userModel.setUsername("test username update");
        restTemplate.put(updateUrl, userModel, UserBoundary.class);
        String expected = "test username update";
        String actual = Objects.requireNonNull(restTemplate.getForObject(loginUrl, UserBoundary.class)).getUsername();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllUsers() throws Exception{
        List<UserBoundary> inDb = IntStream
                .range(0, 5)
                .mapToObj(index-> MockCreator.buildMockUserRequestModel())
                .map(userModel-> restTemplate.postForObject(endpointUrl, userModel, UserBoundary.class))
                .collect(Collectors.toList());

        UserBoundary[] actualObjects = this.restTemplate.getForObject(endpointUrl, UserBoundary[].class);

        assertThat(actualObjects)
                .hasSize(5)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(inDb);
    }

    @Test
    public void deleteAllUsers()  throws JsonProcessingException , Exception{
        IntStream.range(0, 5)
                .mapToObj(index-> MockCreator.buildMockUserRequestModel())
                .forEach(userModel-> restTemplate.postForObject(endpointUrl, userModel, UserBoundary.class));

        this.restTemplate.delete(this.endpointUrl);
        UserBoundary[] actualObjects = this.restTemplate.getForObject(endpointUrl, UserBoundary[].class);

        assertThat(actualObjects).hasSize(0);
    }

}
