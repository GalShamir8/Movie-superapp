package superapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import superapp.logic.ObjectsPageableSearchableService;
import superapp.models.ObjectId;
import superapp.requestModels.ObjectBoundaryModel;
import superapp.view.ObjectBoundary;

import java.util.Date;
import java.util.List;

import static superapp.common.Constants.*;

@RestController
public class SuperAppObjectsController {


    private final static String CREATE_OBJECT_URL = BASE_URL + "/objects";
    private final static String UPDATE_OBJECT_URL = BASE_URL + "/objects" + "/{superapp}" + "/{internalObjectId}";
    private final static String SHOW_URL = BASE_URL + "/objects" + "/{superapp}" + "/{internalObjectId}";
    private final static String GET_ALL_OBJECTS_URL = CREATE_OBJECT_URL;
    private final static String SEARCH_BY_TYPE_URL = BASE_URL + "/objects" + "/searchbyType" + "/{type}";
    private final static String SEARCH_BY_ALIAS_URL = BASE_URL + "/objects" + "/searchbyAlias" + "/{alias}";
    private final static String SEARCH_BY_ALIAS_CONTAINING_URL = BASE_URL + "/objects" + "/searchbyAliasContaining" + "/{text}";
    private final static String SEARCH_BY_CREATED_AT_URL = BASE_URL + "/objects" + "/searchbyCreation" + "/{creationEnum}";
    private final static String BIND_CHILD_TO_OBJECT = BASE_URL + "/objects" + "/{superapp}" + "/{internalObjectId}" + "/children";
    private final static String GET_OBJECT_PARENTS = BASE_URL + "/objects" + "/{superapp}" + "/{internalObjectId}" + "/parents";


    private final ObjectsPageableSearchableService objectService;

    @Autowired
    public SuperAppObjectsController(ObjectsPageableSearchableService objectService) {
        this.objectService = objectService;
    }

    @RequestMapping(
            path = CREATE_OBJECT_URL,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ObjectBoundary create(@RequestBody ObjectBoundaryModel obj) {
        return objectService.createObject(
                new ObjectBoundary(new ObjectId(),
                        obj.getType(),
                        obj.getAlias(),
                        obj.isActive(),
                        new Date(),
                        obj.getCreatedBy(),
                        obj.getObjectDetails())
        );
    }

    @RequestMapping(
            path = UPDATE_OBJECT_URL,
            method = RequestMethod.PUT,
            params = {"userSuperapp", "userEmail"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void updateV1(@RequestBody ObjectBoundary obj,
                         @PathVariable("superapp") String superapp,
                         @PathVariable("internalObjectId") String internalObjectId,
                         @RequestParam("userSuperapp") String userSuperapp,
                         @RequestParam("userEmail") String email) {

        objectService.updateObject(obj, superapp, internalObjectId, userSuperapp, email);
    }

    @RequestMapping(
            path = UPDATE_OBJECT_URL,
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ObjectBoundary update(@RequestBody ObjectBoundaryModel obj,
                                 @PathVariable("superapp") String superapp,
                                 @PathVariable("internalObjectId") String internalObjectId) {
        return objectService.updateObject(superapp, internalObjectId, new ObjectBoundary());
    }


    @RequestMapping(
            path = SHOW_URL,
            method = RequestMethod.GET,
            params = {"superapp", "internalObjectId"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ObjectBoundary show(@PathVariable("superapp") String superapp,
                               @PathVariable("internalObjectId") String internalObjectId) {
        return objectService.getSpecificObject(superapp, internalObjectId);
    }

    @RequestMapping(
            path = SHOW_URL,
            method = RequestMethod.GET,
            params = {"userSuperapp", "userEmail"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ObjectBoundary showV1(@PathVariable("superapp") String superapp,
                                 @PathVariable("internalObjectId") String internalObjectId,
                                 @RequestParam("userSuperapp") String userSuperapp,
                                 @RequestParam("userEmail") String email) {

        return objectService.getSpecificObject(superapp, internalObjectId, userSuperapp, email);
    }

    @RequestMapping(
            path = GET_ALL_OBJECTS_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> index() {
        return objectService.getAllObjects();
    }

    @RequestMapping(
            path = GET_ALL_OBJECTS_URL,
            method = RequestMethod.GET,
            params = {"userSuperapp", "userEmail", "size", "page"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> indexV1(@RequestParam("userSuperapp") String userSuperapp,
                                        @RequestParam("userEmail") String email,
                                        @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return objectService.getAllObjects(userSuperapp, email, page, size);
    }

    @RequestMapping(
            path = SEARCH_BY_TYPE_URL,
            method = RequestMethod.GET,
            params = {"userSuperapp", "userEmail", "size", "page"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> searchByType(@PathVariable("type") String type,
                                             @RequestParam("userSuperapp") String userSuperapp,
                                             @RequestParam("userEmail") String email,
                                             @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                             @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return objectService.searchByType(type, userSuperapp, email, page, size);
    }

    @RequestMapping(
            path = SEARCH_BY_ALIAS_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> searchByAlias(@PathVariable("alias") String alias,
                                              @RequestParam("userSuperapp") String userSuperapp,
                                              @RequestParam("userEmail") String email,
                                              @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                              @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return objectService.searchByAlias(alias, userSuperapp, email, page, size);
    }

    @RequestMapping(
            path = SEARCH_BY_ALIAS_CONTAINING_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> searchByAliasContaining(@PathVariable("text") String text,
                                                        @RequestParam("userSuperapp") String userSuperapp,
                                                        @RequestParam("userEmail") String email,
                                                        @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return objectService.searchByAliasLike(text, userSuperapp, email, page, size);
    }

    @RequestMapping(
            path = SEARCH_BY_CREATED_AT_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> searchByCreatedAt(@PathVariable("creationEnum") String creationEnum,
                                                  @RequestParam("userSuperapp") String userSuperapp,
                                                  @RequestParam("userEmail") String email,
                                                  @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                                  @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return objectService.searchByCreatedAt(creationEnum, userSuperapp, email, page, size);
    }

    @RequestMapping(
            path = BIND_CHILD_TO_OBJECT,
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void bindChildObjectToObject(@RequestBody ObjectId superAppObjectIdBoundary,
                                        @PathVariable("superapp") String superapp,
                                        @PathVariable("internalObjectId") String internalObjectId) {
        objectService.bindChildObjectToObject(superAppObjectIdBoundary, superapp, internalObjectId);

    }

    @RequestMapping(
            path = BIND_CHILD_TO_OBJECT,
            method = RequestMethod.PUT,
            params = {"userSuperapp", "userEmail"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void bindChildObjectToObjectV1(@RequestBody ObjectId superAppObjectIdBoundary,
                                          @PathVariable("superapp") String superapp,
                                          @PathVariable("internalObjectId") String internalObjectId,
                                          @RequestParam("userSuperapp") String userSuperapp,
                                          @RequestParam("userEmail") String email) {
        objectService.bindChildObjectToObject(superAppObjectIdBoundary, superapp, internalObjectId, userSuperapp, email);
    }

    @RequestMapping(
            path = BIND_CHILD_TO_OBJECT,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> getAllChildrenOfObject(@PathVariable("superapp") String superapp,
                                                       @PathVariable("internalObjectId") String internalObjectId) {

        return objectService.getChildrenOfObject(superapp, internalObjectId);
    }

    @RequestMapping(
            path = BIND_CHILD_TO_OBJECT,
            method = RequestMethod.GET,
            params = {"userSuperapp", "userEmail", "size", "page"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> getAllChildrenOfObjectV1(@PathVariable("superapp") String superapp,
                                                         @PathVariable("internalObjectId") String internalObjectId,
                                                         @RequestParam("userSuperapp") String userSuperapp,
                                                         @RequestParam("userEmail") String email,
                                                         @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                                         @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {

        return objectService.getChildrenOfObject(superapp, internalObjectId, userSuperapp, email, size, page);
    }

    @RequestMapping(
            path = GET_OBJECT_PARENTS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> getObjectParents(@PathVariable("superapp") String superapp,
                                                 @PathVariable("internalObjectId") String internalObjectId) {
        return objectService.getParentsOfObject(superapp, internalObjectId);
    }

    @RequestMapping(
            path = GET_OBJECT_PARENTS,
            method = RequestMethod.GET,
            params = {"userSuperapp", "userEmail", "size", "page"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ObjectBoundary> getObjectParentsV1(@PathVariable("superapp") String superapp,
                                                   @PathVariable("internalObjectId") String internalObjectId,
                                                   @RequestParam("userSuperapp") String userSuperapp,
                                                   @RequestParam("userEmail") String email,
                                                   @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PER_PAGE) int size,
                                                   @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return objectService.getParentsOfObject(superapp, internalObjectId, userSuperapp, email, size, page);
    }

}
