package superapp.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import superapp.dal.IdGeneratorCrud;
import superapp.dal.MiniAppCommandCrud;
import superapp.dal.SuperAppObjectCrud;
import superapp.dal.UsersCrud;
import superapp.fixture.Generator;
import superapp.data.*;
import superapp.logic.helpers.MiniAppCommandHelper;
import superapp.logic.helpers.MovieHelper;
import superapp.logic.helpers.SuperAppObjectHelper;
import superapp.logic.helpers.UserHelper;
import superapp.models.*;
import superapp.myMovies.models.MediaGroup;
import superapp.myMovies.models.Movie;
import superapp.myMovies.models.Trailer;
import superapp.requestModels.MiniAppCommandBoundaryModel;
import superapp.view.MiniAppCommandBoundary;
import superapp.view.ObjectBoundary;
import superapp.view.UserBoundary;

import java.util.*;
import java.util.stream.Collectors;

import static superapp.common.Constants.*;

@Service
public class AdminServiceHandler implements AdminService {
    private final SuperAppObjectCrud objects;
    private final UsersCrud users;
    private final SuperAppObjectHelper superAppObjectHelper;
    private Log logger = LogFactory.getLog(AdminServiceHandler.class);
    private final UserHelper userHelper;
    private final MiniAppCommandCrud commands;
    private final MiniAppCommandHelper miniAppCommandHelper;

    private final RestTemplate dataFetcher;

    private final IdGeneratorCrud idGenerator;


    @Autowired
    public AdminServiceHandler(
            ObjectsService objectsService,
            UserHelper userHelper, UsersCrud users,
            SuperAppObjectHelper superAppObjectHelper, SuperAppObjectCrud objects,
            MiniAppCommandHelper miniAppCommandHelper, MiniAppCommandCrud commands,
            IdGeneratorCrud idGenerator) {
        this.userHelper = userHelper;
        this.users = users;
        this.objects = objects;
        this.superAppObjectHelper = superAppObjectHelper;
        this.miniAppCommandHelper = miniAppCommandHelper;
        this.commands = commands;
        this.idGenerator = idGenerator;
        dataFetcher = new RestTemplate();

    }

    @Override
    public void deleteAllUsers(String userSuperApp, String userEmail) {
        logger.debug("admin deleteAllUsers about to be executed");
        makeAuthorisationChecks(userSuperApp, userEmail);
        logger.debug("admin deleteAllUsers user has been Authorized");
        users.deleteAll();
        logger.trace("admin deleteAllUsers All users have been deleted");
    }

    @Override
    public void deleteAllObjects(String userSuperApp, String userEmail) {
        logger.debug("admin deleteAllObjects about to be executed");
        makeAuthorisationChecks(userSuperApp, userEmail);
        logger.debug("admin deleteAllObjects user has been Authorized");
        objects.deleteAll();
        logger.trace("admin deleteAllObjects All objects have been deleted");
    }


//    @Transactional(readOnly = false)
//    @Override
//    public MiniAppCommandBoundary initCommand(String userSuperApp, String userEmail) throws ResponseStatusException, JsonProcessingException {
//        ArrayList<MiniAppCommandBoundaryModel> commands = new ArrayList<>();
//        String[] miniApps = {
//                ADD_TO_FAVORITE,
//                GET_FAVORITES,
//                REMOVE_FAVORITE,
//                ADD_TO_WISHLIST,
//                GET_WISHLISTS,
//                REMOVE_WISHLIST
//        };
//
//        for(String miniApp: miniApps) {
//            IdGeneratorEntity helper = this.idGenerator.save(new IdGeneratorEntity());
//            this.idGenerator.delete(helper);
//            String commandStr = miniApp.contains("Favorite") ? "Favorites" : "Wishlist";
//
//            MiniAppCommandBoundaryModel command = new MiniAppCommandBoundaryModel(
//                    commandStr,
//                    new TargetObject(),
//                    new InvokedBy(),
//                    new HashMap<>()
//            );
//            commands.add(command);
//            MiniAppCommandEntity miniAppCommandEntity = miniAppCommandHelper.buildEntityFromBoundary(miniApp, command);
//            this.commands.save(miniAppCommandEntity);
//        }
//
//        return command;
//    }


    @Override
    public void deleteAllCommandsHistory(String userSuperApp, String userEmail) {
        logger.debug("admin deleteAllCommandsHistory about to be executed");
        makeAuthorisationChecks(userSuperApp, userEmail);
        logger.debug("admin deleteAllCommandsHistory user has been Authorized");
        logger.trace("admin deleteAllCommandsHistory All commands have been deleted");
        commands.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserBoundary> exportAllUsers(String userSuperApp, String userEmail, int size, int page) {
        logger.debug("admin exportAllUsers about to be executed");
        makeAuthorisationChecks(userSuperApp, userEmail);
        logger.debug("admin exportAllUsers user has been Authorized");
        logger.trace("admin exportAllUsers is being executed...");
        return users
                .findAll(PageRequest.of(page, size))
                .stream()
                .map(userHelper::buildBoundaryFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MiniAppCommandBoundary> exportAllCommandsHistory(String userSuperApp, String userEmail, int size, int page) {
        logger.debug("admin exportAllCommandsHistory about to be executed");
        makeAuthorisationChecks(userSuperApp, userEmail);
        logger.debug("admin exportAllCommandsHistory user has been Authorized");
        logger.trace("admin exportAllCommandsHistory is being executed...");
        return commands
                .findAll(PageRequest.of(page, size))
                .stream()
                .map(miniAppCommandHelper::buildBoundaryFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MiniAppCommandBoundary> exportSpecificCommandsHistory(String userSuperApp, String userEmail, String miniAppName, int size, int page) throws ResponseStatusException {
        logger.debug("admin exportSpecificCommandsHistory about to be executed");
        makeAuthorisationChecks(userSuperApp, userEmail);
        logger.debug("admin exportSpecificCommandsHistory user has been Authorized");
        logger.trace("admin exportSpecificCommandsHistory is being executed...");
        return commands
                .findAllByCommand(PageRequest.of(page, size), miniAppName)
                .stream()
                .map(this.miniAppCommandHelper::buildBoundaryFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String updateDataBase(String userSuperApp, String userEmail) throws JsonProcessingException {
        String id = userHelper.generateUserId(userSuperApp, userEmail);
        logger.debug("updateDataBase About to init Database");

        makeAuthorisationChecks(userSuperApp, userEmail);
        for (int i = 1; i <= 10; i++) {
            MediaGroup upcoming = superAppObjectHelper.buildMediaGroupFromMap(dataFetcher.getForObject(
                    "https://api.themoviedb.org/3/movie/upcoming?language=en&api_key=b3b1492d3e91e9f9403a2989f3031b0c&size=20&page=" + i,
                    Map.class));
            for (Movie m : upcoming.getResults()) {
                addMovieTrailer(m);
                SuperAppObjectEntity superAppObjectEntity = superAppObjectHelper.buildEntityFromBoundary(
                        new ObjectBoundary(
                                new ObjectId(),
                                MovieHelper.Type,
                                MovieHelper.Upcoming,
                                true,
                                new Date(),
                                new UserId(userSuperApp, userEmail),
                                superAppObjectHelper.buildMapFromMovie(m)));
                objects.save(superAppObjectEntity);
            }

            MediaGroup topRated = superAppObjectHelper.buildMediaGroupFromMap(dataFetcher.getForObject(
                    "https://api.themoviedb.org/3/movie/top_rated?language=en&api_key=b3b1492d3e91e9f9403a2989f3031b0c&size=20&page=" + i,
                    Map.class));
            for (Movie m : topRated.getResults()) {
                addMovieTrailer(m);
                SuperAppObjectEntity superAppObjectEntity = superAppObjectHelper.buildEntityFromBoundary(
                        new ObjectBoundary(
                                new ObjectId(),
                                MovieHelper.Type,
                                MovieHelper.TopRated,
                                true,
                                new Date(),
                                new UserId(userSuperApp, userEmail),
                                superAppObjectHelper.buildMapFromMovie(m)));
                objects.save(superAppObjectEntity);
            }
            MediaGroup nowPlaying = superAppObjectHelper.buildMediaGroupFromMap(dataFetcher.getForObject(
                    "https://api.themoviedb.org/3/movie/now_playing?language=en&api_key=b3b1492d3e91e9f9403a2989f3031b0c&size=20&page=" + i,
                    Map.class));
            for (Movie m : nowPlaying.getResults()) {
                addMovieTrailer(m);
                SuperAppObjectEntity superAppObjectEntity = superAppObjectHelper.buildEntityFromBoundary(
                        new ObjectBoundary(
                                new ObjectId(),
                                MovieHelper.Type,
                                MovieHelper.NowPlaying,
                                true,
                                new Date(),
                                new UserId(userSuperApp, userEmail),
                                superAppObjectHelper.buildMapFromMovie(m)));
                objects.save(superAppObjectEntity);
            }
        }
        logger.trace("updateDataBase Database initialization completed!");
        return "Success!";
    }

    private void addMovieTrailer(Movie m) {
        try {
            if (m.isVideo()) {
                String endpointUrl = "https://api.themoviedb.org/3/movie/:movieId/videos?api_key=b3b1492d3e91e9f9403a2989f3031b0c&language=en";
                Map<String, List<Trailer>> res = dataFetcher.getForObject(endpointUrl, Map.class);
                if (res == null)
                    return;

                for (Trailer t: res.get("results").stream().toList()){
                    if (t.getSite().equals("YouTube")){
                        m.setTrailerUrl(String.format("https://www.youtube.com/watch?v=%s", t.getKey()));
                    }
                }
            }
        }catch (Exception e){ }
    }

    private void makeAuthorisationChecks(String userSuperApp, String userEmail) {
        String id = userHelper.generateUserId(userSuperApp, userEmail);
        Optional<UserEntity> user = users.findById(id);
        if (user.isEmpty()) {
            logger.warn(USER_NOT_FOUND_MSG);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG);
        }
        if (!user.get().getRole().equals(UserRole.ADMIN)) {
            logger.warn(USER_NOT_AUTHORISED+": name: "+userSuperApp+" email: "+userEmail);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_NOT_AUTHORISED);
        }
    }
}
