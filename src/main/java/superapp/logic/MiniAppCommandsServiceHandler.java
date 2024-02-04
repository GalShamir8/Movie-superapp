//package superapp.logic;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//import superapp.common.Helper;
//import superapp.dal.SuperAppObjectCrud;
//import superapp.dal.UsersCrud;
//import superapp.data.MiniAppCommandEntity;
//import superapp.data.SuperAppObjectEntity;
//import superapp.data.UserEntity;
//import superapp.logic.helpers.MiniAppCommandHelper;
//import superapp.logic.helpers.MovieHelper;
//import superapp.requestModels.MiniAppCommandBoundaryModel;
//import superapp.view.MiniAppCommandBoundary;
//import superapp.dal.MiniAppCommandCrud;
//
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//import static superapp.common.Constants.*;
//
//
//@Service
//public class MiniAppCommandsServiceHandler implements MiniAppCommandsService {
//    private final Map<String, List<MiniAppCommandEntity>> miniAppCommands;
//    private Map<String, MiniAppCommandEntity> commands;
//    private final MiniAppCommandCrud commands;
//    private Log logger = LogFactory.getLog(MiniAppCommandsServiceHandler.class);
//    private final UsersService userService;
//    private final UsersCrud users;
//    private final SuperAppObjectCrud objects;
//    private final MiniAppCommandHelper miniAppCommandHelper;
//    private final  ObjectsPageableSearchableService objectsService;
//
//
//    @Autowired
//    public MiniAppCommandsServiceHandler(MiniAppCommandHelper miniAppCommandHelper,
//                                         MiniAppCommandCrud commands, UsersService userService ,
//                                         ObjectsPageableSearchableService objectsService,
//                                         UsersCrud users,
//                                         SuperAppObjectCrud objects) {
//        this.miniAppCommands = new HashMap<>();
//        this.miniAppCommandHelper = miniAppCommandHelper;
//        this.commands = commands;
//        this.userService = userService;
//        this.objectsService = objectsService;
//        this.users = users;
//        this.objects = objects;
//    }
//
//    @Override
//    @Transactional
//    public void deleteAllCommands() {
//        logger.debug("MiniAppCommandsServiceHandler deleteAllCommands about to be executed");
//        this.commands.deleteAll();
//        logger.trace("MiniAppCommandsServiceHandler deleteAllCommands All commands have been deleted");
//
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<MiniAppCommandBoundary> getAllCommands() {
//        logger.debug("MiniAppCommandsServiceHandler getAllCommands about to be executed");
//        logger.trace("MiniAppCommandsServiceHandler getAllCommands Getting commands...");
//        return StreamSupport.stream(this.commands.findAll().spliterator(), false).
//                map(this.miniAppCommandHelper::buildBoundaryFromEntity).collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public Object invokeCommand(String miniAppName, MiniAppCommandBoundaryModel command) throws Exception {
//        MiniAppCommandEntity miniAppCommandEntity = miniAppCommandHelper.buildEntityFromBoundary(miniAppName, command);
//        saveCommand(miniAppCommandEntity);
//        Set<SuperAppObjectEntity> res;
//
//        switch (miniAppName){
//            case ADD_TO_FAVORITE:
//                logger.trace("MiniAppCommandsServiceHandler invoking Command" + ADD_TO_FAVORITE);
//                addToFav(command);
//                break;
//            case GET_FAVORITES:
//                logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromWishListStr);
//                res = getFav(command);
//                return res.stream().map(miniAppCommandHelper::buildsuperAppBoundaryFromEntity).toList();
//            case REMOVE_FAVORITE:
//                logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromWishListStr);
//                removeFromFav(command);
//                break;
//            case ADD_TO_WISHLIST:
//                logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromWishListStr);
//                addToWishlist(command);
//                break;
//            case GET_WISHLISTS:
//                logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromWishListStr);
//                res = getWishlist(command);
//                return res.stream().map(miniAppCommandHelper::buildsuperAppBoundaryFromEntity).toList();
//            case REMOVE_WISHLIST:
//                logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromWishListStr);
//                removeFromWishlist(command);
//                break;
//        }
//        return null;
//    }
//
//    private void removeFromWishlist(MiniAppCommandBoundaryModel command) {
//        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
//        String userEmail = command.getInvokedBy().getUserId().getEmail();
//        UserEntity userEntity = getUser(userSuperApp, userEmail);
//        String superApp = command.getTargetObject().getObjectId().getSuperApp();
//        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
//        SuperAppObjectEntity movie = getSuperappObject(superApp, internalObjectId);
//        Set<SuperAppObjectEntity> wishlist = userEntity.getWishlist();
//        wishlist.remove(movie);
//    }
//
//    private Set<SuperAppObjectEntity> getWishlist(MiniAppCommandBoundaryModel command) {
//        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
//        String userEmail = command.getInvokedBy().getUserId().getEmail();
//        UserEntity userEntity = getUser(userSuperApp, userEmail);
//
//        return userEntity.getWishlist();
//    }
//
//    private void addToWishlist(MiniAppCommandBoundaryModel command) {
//        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
//        String userEmail = command.getInvokedBy().getUserId().getEmail();
//        UserEntity userEntity = getUser(userSuperApp, userEmail);
//        String superApp = command.getTargetObject().getObjectId().getSuperApp();
//        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
//        SuperAppObjectEntity wishlist = getSuperappObject(superApp, internalObjectId);
//        userEntity.addToWishlist(wishlist);
//    }
//
//    private void removeFromFav(MiniAppCommandBoundaryModel command) {
//        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
//        String userEmail = command.getInvokedBy().getUserId().getEmail();
//        UserEntity userEntity = getUser(userSuperApp, userEmail);
//        String superApp = command.getTargetObject().getObjectId().getSuperApp();
//        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
//        SuperAppObjectEntity movie = getSuperappObject(superApp, internalObjectId);
//        Set<SuperAppObjectEntity> favorites = userEntity.getFavorites();
//        favorites.remove(movie);
//    }
//
//    private Set<SuperAppObjectEntity> getFav(MiniAppCommandBoundaryModel command) {
//        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
//        String userEmail = command.getInvokedBy().getUserId().getEmail();
//        UserEntity userEntity = getUser(userSuperApp, userEmail);
//
//        return userEntity.getFavorites();
//    }
//
//    private void addToFav(MiniAppCommandBoundaryModel command) {
//        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
//        String userEmail = command.getInvokedBy().getUserId().getEmail();
//        UserEntity userEntity = getUser(userSuperApp, userEmail);
//        String superApp = command.getTargetObject().getObjectId().getSuperApp();
//        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
//        SuperAppObjectEntity fav = getSuperappObject(superApp, internalObjectId);
//        userEntity.addFavorite(fav);
//    }
//    @Transactional(readOnly = false)
//    public Object invokeCommand(String miniAppName, MiniAppCommandBoundary command) throws Exception {
//        logger.debug("MiniAppCommandsServiceHandler invokeCommand command: "+miniAppName+" is about to be invoked");
//        MiniAppCommandEntity miniAppCommandEntity = miniAppCommandHelper.buildEntityFromBoundary(command);
//        saveCommand(miniAppCommandEntity, command.getCommandId().getMiniApp());
//        logger.trace("MiniAppCommandsServiceHandler invokeCommand Command is being invoked");
//        return invoke(miniAppName, miniAppCommandEntity);
//
//    }
//
//    //todo clean here
//    private Object invoke(String miniAppName, MiniAppCommandEntity commandEntity) throws Exception {
//            switch (miniAppName) {
//                case MovieHelper.AddToWishListStr:
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.AddToWishListStr);
//                    return new AddToWishList(commandEntity, userService,  objectsService).invoke();
//
//                case MovieHelper.RemoveFromWishListStr :
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromWishListStr);
//                    return new RemoveFromWishList(commandEntity, userService,objectsService).invoke();
//
//                case MovieHelper.AddToFavoriteStr:
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.AddToFavoriteStr);
//                    return new AddToFavorite(commandEntity, userService,  objectsService).invoke();
//
//                case MovieHelper.RemoveFromFavoriteStr :
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromFavoriteStr);
//                    return new RemoveFromFavorite(commandEntity, userService,objectsService).invoke();
//
//                case MovieHelper.AddToWatchedStr:
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.AddToWatchedStr);
//                    return new AddToWatch(commandEntity, userService,  objectsService).invoke();
//
//                case MovieHelper.RemoveFromWatchedStr :
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.RemoveFromWatchedStr);
//                    return new RemoveFromWatch(commandEntity, userService,objectsService).invoke();
//
//                case MovieHelper.GetAllWishList:
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.GetAllWishList);
//                    return new GetAllWishList(commandEntity, userService, objectsService).invoke();
//
//                case MovieHelper.GetAllFavorite:
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.GetAllFavorite);
//                    return new GetAllFavorite(commandEntity, userService, objectsService).invoke();
//
//                case MovieHelper.GetAllWatched:
//                    logger.trace("MiniAppCommandsServiceHandler invoking Command" + MovieHelper.GetAllWatched);
//                    return new GetAllWatched(commandEntity, userService, objectsService).invoke();
//
//
//
//                default:
//                    return null;
//
//    private SuperAppObjectEntity getSuperappObject(String superApp, String internalObjectId) {
//        Optional<SuperAppObjectEntity> superAppObjectEntity = objects.findById(superApp + "_" + internalObjectId);
//        if (superAppObjectEntity.isEmpty())
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SUPER_APP_OBJECT_NOT_FOUND_MSG);
//        return superAppObjectEntity.get();
//    }
//
//    private UserEntity getUser(String userSuperApp, String userEmail) {
//        Optional<UserEntity> userEntity = users.findById(generateUserId(userSuperApp, userEmail));
//        if (userEntity.isEmpty())
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG);
//        return userEntity.get();
//    }
//
//    public String generateUserId(String superApp, String userEmail) {
//        String idComb = superApp + "_" + userEmail;
//        return Helper.generateUuid(idComb);
//    }
//
//    @Transactional
//    public void saveCommand(MiniAppCommandEntity miniAppCommandEntity, String miniApp) {
//        logger.debug("MiniAppCommandsServiceHandler saveCommand is about to be executed on command:"+ miniAppCommandEntity);
//        this.commands.save(miniAppCommandEntity);
//        logger.trace("MiniAppCommandsServiceHandler saveCommand saveCommand command is saved ->"+ miniAppCommandEntity);
//        miniAppCommands.computeIfAbsent(miniApp, v -> new ArrayList<>());
//        miniAppCommands.get(miniApp).add(miniAppCommandEntity);
//    }
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) throws ResponseStatusException {
//            return commands.findAllByCommand(miniAppName)
//                            .stream()
//                            .map(miniAppCommandHelper::buildBoundaryFromEntity)
//                            .toList();
//        logger.debug("MiniAppCommandsServiceHandler getAllMiniAppCommands is about to be executed ");
//        logger.trace("MiniAppCommandsServiceHandler getAllMiniAppCommands Getting all" +miniAppName+ " Commands");
//        return miniAppCommands
//                .values()
//                .stream()
//                .flatMap(List::stream)
//                .map(miniAppCommandHelper::buildBoundaryFromEntity)
//                .collect(Collectors.toList());
//    }
//}
package superapp.logic;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import superapp.common.Helper;
import superapp.dal.SuperAppObjectCrud;
import superapp.dal.UsersCrud;
import superapp.data.MiniAppCommandEntity;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.logic.helpers.MiniAppCommandHelper;
import superapp.requestModels.MiniAppCommandBoundaryModel;
import superapp.view.MiniAppCommandBoundary;
import superapp.dal.MiniAppCommandCrud;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static superapp.common.Constants.*;


@Service
public class MiniAppCommandsServiceHandler implements MiniAppCommandsService {
    private final Map<String, List<MiniAppCommandEntity>> miniAppCommands;
    //    private Map<String, MiniAppCommandEntity> commands;
    private final MiniAppCommandCrud commands;
    private final UsersService userService;
    private final UsersCrud users;
    private Log logger = LogFactory.getLog(MiniAppCommandsServiceHandler.class);
    private final SuperAppObjectCrud objects;
    private final MiniAppCommandHelper miniAppCommandHelper;
    private final  ObjectsPageableSearchableService objectsService;


    @Autowired
    public MiniAppCommandsServiceHandler(MiniAppCommandHelper miniAppCommandHelper,
                                         MiniAppCommandCrud commands, UsersService userService ,
                                         ObjectsPageableSearchableService objectsService,
                                         UsersCrud users,
                                         SuperAppObjectCrud objects) {
        this.miniAppCommands = new HashMap<>();
        this.miniAppCommandHelper = miniAppCommandHelper;
        this.commands = commands;
        this.userService = userService;
        this.objectsService = objectsService;
        this.users = users;
        this.objects = objects;
    }

    @Override
    @Transactional
    public void deleteAllCommands() {
        logger.debug("MiniAppCommandsServiceHandler deleteAllCommands about to be executed");
        this.commands.deleteAll();
        logger.trace("MiniAppCommandsServiceHandler deleteAllCommands All Commands deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllCommands() {
        logger.debug("MiniAppCommandsServiceHandler getAllCommands about to be executed");
        logger.trace("MiniAppCommandsServiceHandler getAllCommands Getting All Commands...");
        return StreamSupport.stream(this.commands.findAll().spliterator(), false).
                map(this.miniAppCommandHelper::buildBoundaryFromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Object invokeCommand(String miniAppName, MiniAppCommandBoundaryModel command) throws Exception {
        MiniAppCommandEntity miniAppCommandEntity = miniAppCommandHelper.buildEntityFromBoundary(miniAppName, command);
        saveCommand(miniAppCommandEntity);
        Set<SuperAppObjectEntity> res;

        switch (miniAppName){
            case ADD_TO_FAVORITE:
                logger.trace("MiniAppCommandsServiceHandler invoking Command" + ADD_TO_FAVORITE);
                addToFav(command);
                break;
            case GET_FAVORITES:
                logger.trace("MiniAppCommandsServiceHandler invoking Command" + GET_FAVORITES);
                res = getFav(command);
                return res.stream().map(miniAppCommandHelper::buildsuperAppBoundaryFromEntity).toList();
            case REMOVE_FAVORITE:
                logger.trace("MiniAppCommandsServiceHandler invoking Command" + REMOVE_FAVORITE);
                removeFromFav(command);
                break;
            case ADD_TO_WISHLIST:
                logger.trace("MiniAppCommandsServiceHandler invoking Command" + ADD_TO_WISHLIST);
                addToWishlist(command);
                break;
            case GET_WISHLISTS:
                logger.trace("MiniAppCommandsServiceHandler invoking Command" + GET_WISHLISTS);
                res = getWishlist(command);
                return res.stream().map(miniAppCommandHelper::buildsuperAppBoundaryFromEntity).toList();
            case REMOVE_WISHLIST:
                logger.trace("MiniAppCommandsServiceHandler invoking Command" + REMOVE_WISHLIST);
                removeFromWishlist(command);
                break;
        }
        return null;
    }

    private void removeFromWishlist(MiniAppCommandBoundaryModel command) {
        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
        String userEmail = command.getInvokedBy().getUserId().getEmail();
        UserEntity userEntity = getUser(userSuperApp, userEmail);
        String superApp = command.getTargetObject().getObjectId().getSuperApp();
        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
        SuperAppObjectEntity movie = getSuperappObject(superApp, internalObjectId);
        Set<SuperAppObjectEntity> wishlist = userEntity.getWishlist();
        wishlist.remove(movie);
        logger.trace("MiniAppCommandsServiceHandler removeFromWishlist Command" + REMOVE_WISHLIST + " Invoked successfully");
    }

    private Set<SuperAppObjectEntity> getWishlist(MiniAppCommandBoundaryModel command) {
        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
        String userEmail = command.getInvokedBy().getUserId().getEmail();
        UserEntity userEntity = getUser(userSuperApp, userEmail);
        logger.trace("MiniAppCommandsServiceHandler getWishlist" + GET_WISHLISTS + " Invoked successfully");
        return userEntity.getWishlist();
    }

    private void addToWishlist(MiniAppCommandBoundaryModel command) {
        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
        String userEmail = command.getInvokedBy().getUserId().getEmail();
        UserEntity userEntity = getUser(userSuperApp, userEmail);
        String superApp = command.getTargetObject().getObjectId().getSuperApp();
        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
        SuperAppObjectEntity wishlist = getSuperappObject(superApp, internalObjectId);
        logger.trace("MiniAppCommandsServiceHandler addToWishlist" + ADD_TO_WISHLIST + " Invoked successfully");
        userEntity.addToWishlist(wishlist);
    }

    private void removeFromFav(MiniAppCommandBoundaryModel command) {
        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
        String userEmail = command.getInvokedBy().getUserId().getEmail();
        UserEntity userEntity = getUser(userSuperApp, userEmail);
        String superApp = command.getTargetObject().getObjectId().getSuperApp();
        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
        SuperAppObjectEntity movie = getSuperappObject(superApp, internalObjectId);
        Set<SuperAppObjectEntity> favorites = userEntity.getFavorites();
        logger.trace("MiniAppCommandsServiceHandler removeFromFav" + REMOVE_FAVORITE + " Invoked successfully");
        favorites.remove(movie);
    }

    private Set<SuperAppObjectEntity> getFav(MiniAppCommandBoundaryModel command) {
        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
        String userEmail = command.getInvokedBy().getUserId().getEmail();
        UserEntity userEntity = getUser(userSuperApp, userEmail);
        logger.trace("MiniAppCommandsServiceHandler getFav" + GET_FAVORITES + " Invoked successfully");
        return userEntity.getFavorites();
    }

    private void addToFav(MiniAppCommandBoundaryModel command) {
        String userSuperApp = command.getInvokedBy().getUserId().getSuperapp();
        String userEmail = command.getInvokedBy().getUserId().getEmail();
        UserEntity userEntity = getUser(userSuperApp, userEmail);
        String superApp = command.getTargetObject().getObjectId().getSuperApp();
        String internalObjectId = command.getTargetObject().getObjectId().getInternalObjectId();
        SuperAppObjectEntity fav = getSuperappObject(superApp, internalObjectId);
        logger.trace("MiniAppCommandsServiceHandler addToFav" + ADD_TO_FAVORITE + " Invoked successfully");
        userEntity.addFavorite(fav);
    }

    private SuperAppObjectEntity getSuperappObject(String superApp, String internalObjectId) {
        logger.trace("MiniAppCommandsServiceHandler getSuperappObject Getting Object with the id of:"+internalObjectId);
        Optional<SuperAppObjectEntity> superAppObjectEntity = objects.findById(superApp + "_" + internalObjectId);
        if (superAppObjectEntity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SUPER_APP_OBJECT_NOT_FOUND_MSG);
        return superAppObjectEntity.get();
    }

    private UserEntity getUser(String userSuperApp, String userEmail) {
        logger.trace("MiniAppCommandsServiceHandler getUser Getting User with the email :"+userEmail);
        Optional<UserEntity> userEntity = users.findById(generateUserId(userSuperApp, userEmail));
        if (userEntity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG);
        return userEntity.get();
    }

    public String generateUserId(String superApp, String userEmail) {
        String idComb = superApp + "_" + userEmail;
        return Helper.generateUuid(idComb);
    }
    @Transactional
    public void saveCommand(MiniAppCommandEntity miniAppCommandEntity) {
        logger.debug("MiniAppCommandsServiceHandler saveCommand Saving Command:"+miniAppCommandEntity);
        this.commands.save(miniAppCommandEntity);
        logger.trace("MiniAppCommandsServiceHandler saveCommand Command: "+miniAppCommandEntity +" Has been saved");
    }


    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) throws ResponseStatusException {
        logger.trace("MiniAppCommandsServiceHandler getAllMiniAppCommands Getting all Commands of: "+miniAppName);
        return commands.findAllByCommand(miniAppName)
                .stream()
                .map(miniAppCommandHelper::buildBoundaryFromEntity)
                .toList();
    }
}
