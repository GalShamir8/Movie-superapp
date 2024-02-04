package superapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import superapp.fixture.Generator;
import superapp.models.CommandId;
import superapp.models.InvokedBy;
import superapp.models.TargetObject;
import superapp.requestModels.UserBoundaryModel;
import superapp.view.MiniAppCommandBoundary;
import superapp.view.UserBoundary;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MiniAppControllerTests {
    private int port;
    private RestTemplate restTemplate;
    private String endpointUrl;
    private final static String BASE_URL =  "http://localhost";

    @LocalServerPort
    public void setPort(int port){
        this.port = port;
    }

    @PostConstruct
    public void init() {
        this.endpointUrl = BASE_URL + ":" + this.port + "/superapp" + "/miniapp";
        this.restTemplate = new RestTemplate();
    }

    @AfterEach
    public void cleanDatabase() {
        // invoke HTTP DELETE
        this.restTemplate
                .delete(this.endpointUrl);
    }

    @Test
    public void testInvoke(){
        MiniAppCommandBoundary miniAppCommand = buildMockMiniAppCommandBoundary();
        String invokeUrl = endpointUrl + miniAppCommand.getCommand();
        //String invokedUrl = String.format(Locale.US, "%s/%s", endpointUrl, miniAppName);
        MiniAppCommandBoundary actualObject = restTemplate.postForObject(invokeUrl, miniAppCommand, MiniAppCommandBoundary.class);

        assertThat(actualObject).isEqualTo(miniAppCommand);
    }

    @Test
    public void testDelete(){
        MiniAppCommandBoundary miniAppCommandBoundary = buildMockMiniAppCommandBoundary();
        String invokeUrl = endpointUrl + miniAppCommandBoundary.getCommand();
        restTemplate.postForObject(invokeUrl, miniAppCommandBoundary, MiniAppCommandBoundary.class); // Adding a miniappCommand
        restTemplate.delete(endpointUrl); // Deleting all commands
        MiniAppCommandBoundary[] remainingMiniAppCommands = restTemplate.getForObject(endpointUrl,MiniAppCommandBoundary[].class); //getting all commands
        assertThat(remainingMiniAppCommands).hasSize(0); // checking if we got an empty array, if so delete succeed

    }

    @Test
    public void testGetAllMiniApps(){
        List<MiniAppCommandBoundary> miniAppList = IntStream // Posting (adding) 5 miniapps
                .range(0, 5)
                .mapToObj(index-> buildMockMiniAppCommandBoundary())
                .map(miniAppModel-> restTemplate.postForObject(endpointUrl+miniAppModel.getCommand(), miniAppModel, MiniAppCommandBoundary.class))
                .collect(Collectors.toList());

        MiniAppCommandBoundary[] actualObjects = this.restTemplate.getForObject(endpointUrl, MiniAppCommandBoundary[].class); //getting all miniaps

        assertThat(actualObjects).hasSize(5) //expecting to get the exact 5 miniapps in any order
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(miniAppList);
    }

    @Test
    public void testGetCommandsForMiniApp(){
        //Todo create miniapp with miniapp name
        MiniAppCommandBoundary miniAppModel = buildMockMiniAppCommandBoundary();
        restTemplate.postForObject(endpointUrl+miniAppModel.getCommand(), miniAppModel, MiniAppCommandBoundary.class);
        MiniAppCommandBoundary[] actualObject = this.restTemplate.getForObject(endpointUrl+miniAppModel.getCommand(), MiniAppCommandBoundary[].class);

        assertThat(actualObject[0]).isEqualTo(miniAppModel);
    }

    private MiniAppCommandBoundary buildMockMiniAppCommandBoundary() {
        new MiniAppCommandBoundary();
        return new MiniAppCommandBoundary(
                new CommandId(),
                Generator.generateString(),
                new TargetObject(),
                Generator.generateDateTime(),
                new InvokedBy(),
                new HashMap<>()
        );
    }

}

