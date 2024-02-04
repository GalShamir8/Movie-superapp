package superapp.logic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import superapp.common.Helper;
import superapp.dal.SuperAppObjectCrud;
import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.logic.helpers.SuperAppObjectHelper;
import superapp.view.ObjectBoundary;
import superapp.models.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import static superapp.common.Constants.*;

@Service
public class ObjectsServiceHandler implements ObjectsPageableSearchableService {

    //private Map<String, SuperAppObjectEntity> objects;
    private SuperAppObjectCrud objects;
    private Log logger = LogFactory.getLog(ObjectsServiceHandler.class);
    private final SuperAppObjectHelper superAppObjectHelper;


    @Autowired
    public ObjectsServiceHandler(SuperAppObjectHelper superAppObjectHelper, SuperAppObjectCrud objects) {
        this.superAppObjectHelper = superAppObjectHelper;
        this.objects = objects;
    }
    @Override
    @Transactional
    public ObjectBoundary createObject(ObjectBoundary object) {
        String userSuperapp = object.getCreatedBy().getSuperapp();
        String email = object.getCreatedBy().getEmail();
        if(validateUserPermission(userSuperapp, email)){
            SuperAppObjectEntity superAppObjectEntity = superAppObjectHelper.buildEntityFromBoundary(object);
            logger.trace("(createObject) Object: "+superAppObjectEntity+" has been created and about to be saved");
            saveObject(superAppObjectEntity);
            return superAppObjectHelper.buildBoundaryFromEntity(superAppObjectEntity);
        }else {
            logger.error("Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private void saveObject(SuperAppObjectEntity superAppObjectEntity) {
        objects.save(superAppObjectEntity);
        logger.trace("Object: "+ superAppObjectEntity + "\nHas been saved to the DataBase ");
    }

    @Override
    @Deprecated
    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update) throws ResponseStatusException {
        logger.error("A Deprecated call has been activated");
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, DEPRECATED_MESSAGE);
    }


    @Override
    @Deprecated
    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) throws ResponseStatusException {
        logger.error("A Deprecated call has been activated");
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, DEPRECATED_MESSAGE);
    }

    private SuperAppObjectEntity getSuperAppObjectEntity(String superapp, String internalObjectId) {
        Optional<SuperAppObjectEntity> optionalEntity = objects.
                findById(superAppObjectHelper.getId(superapp, internalObjectId));
        if (optionalEntity.isEmpty()) {
            logger.error("Object hasn't been found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SUPER_APP_OBJECT_NOT_FOUND_MSG);
        }
        logger.trace("Found the requested object: "+optionalEntity.get());
        return optionalEntity.get();
    }

    @Override
    @Deprecated
    public List<ObjectBoundary> getAllObjects() {
        logger.error("A Deprecated call has been activated getAllObjects");
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, DEPRECATED_MESSAGE);
    }

    @Override
    @Transactional
    public void deleteAllObjects() {
        logger.debug("About to delete all Objects from the database");
        for (SuperAppObjectEntity obj: objects.findAll()){
            obj.deleteAllUsers();
        }
        this.objects.deleteAll();
        logger.trace("All Objects has been deleted from the database");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> searchByType(String type, String userSuperapp, String email, int page, int size) {
        if(validateUserPermission(userSuperapp, email)){
            return objects.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "createdAt", "objectId"))
                            .stream()
                            .map(superAppObjectHelper::buildBoundaryFromEntity)
                            .toList();
        }else{
            logger.error("searchByType Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean validateUserPermission(String userSuperapp, String email) {
        UserEntity userEntity = superAppObjectHelper.getUser(superAppObjectHelper.generateUserId(userSuperapp, email));
        return userEntity.getRole().equals(UserRole.SUPERAPP_USER) || userEntity.getRole().equals(UserRole.ADMIN);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> searchByAlias(String alias, String userSuperapp, String email, int page, int size) {
        if(validateUserPermission(userSuperapp, email)){
            return objects.findAllByAlias(alias, PageRequest.of(page, size, Direction.ASC, "createdAt", "objectId"))
                    .stream()
                    .map(superAppObjectHelper::buildBoundaryFromEntity)
                    .toList();
        }else{
            logger.error("searchByAlias Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> searchByAliasLike(String alias, String userSuperapp, String email, int page, int size) {
        if(validateUserPermission(userSuperapp, email)){
            return objects.findAllByAliasLike("%" + alias + "%", PageRequest.of(page, size, Direction.ASC, "createdAt", "objectId"))
                    .stream()
                    .map(superAppObjectHelper::buildBoundaryFromEntity)
                    .toList();
        }else{
            logger.error("searchByAliasLike Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> searchByCreatedAt(String creationEnum, String userSuperapp, String email, int page, int size) {
        try{
            if(!validateUserPermission(userSuperapp, email)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            } else {
                ArrayList<Date> dateRange = Helper.buildDateBetween(creationEnum);
                return objects.findAllByCreatedAtBetween(dateRange.get(0), dateRange.get(1), PageRequest.of(page, size, Direction.ASC, "createdAt", "objectId"))
                        .stream()
                        .map(superAppObjectHelper::buildBoundaryFromEntity)
                        .toList();
            }
        }catch (IllegalArgumentException e){
            logger.error("searchByCreatedAt Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CREATION_ENUM);
        }
    }

    @Override
    @Transactional
    public void updateObject(ObjectBoundary objBoundary, String superapp, String internalObjectId, String userSuperapp, String email) {
        if(validateUserPermission(userSuperapp, email)){
            Optional<SuperAppObjectEntity> objectEntity = objects.findById(superAppObjectHelper.getId(superapp, internalObjectId));
            if (objectEntity.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, SUPER_APP_OBJECT_NOT_FOUND_MSG);
            saveObject(superAppObjectHelper.updateEntity(objectEntity.get(), objBoundary));
        }else{
            logger.error("updateObject Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ObjectBoundary getSpecificObject(String superapp, String internalObjectId, String userSuperapp, String email) {
        if(validateUserPermission(userSuperapp, email)){
            Optional<SuperAppObjectEntity> objectEntity = objects.findById(superAppObjectHelper.getId(superapp, internalObjectId));
            if (objectEntity.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, SUPER_APP_OBJECT_NOT_FOUND_MSG);
            }
            return superAppObjectHelper.buildBoundaryFromEntity(objectEntity.get());
        }else{
            logger.error("getSpecificObject Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getAllObjects(String userSuperapp, String email, int page, int size) {
        if(validateUserPermission(userSuperapp, email)){
            return objects.findAll(PageRequest.of(page, size, Direction.ASC, "createdAt", "objectId"))
                    .stream()
                    .map(superAppObjectHelper::buildBoundaryFromEntity)
                    .toList();
        }else{
            logger.error("getAllObjects Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Deprecated
    public void bindChildObjectToObject(ObjectId superAppObjectIdBoundary, String superapp, String internalObjectId) {
        logger.error("bindChildObjectToObject A Deprecated call has been activated");
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, DEPRECATED_MESSAGE);
    }

    @Override
    @Deprecated
    public List<ObjectBoundary> getChildrenOfObject(String superapp, String internalObjectId) {
        logger.error("getChildrenOfObject A Deprecated call has been activated");
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, DEPRECATED_MESSAGE);
    }

    @Override
    @Deprecated
    public List<ObjectBoundary> getParentsOfObject(String superapp, String internalObjectId) {
        logger.error("getParentsOfObject A Deprecated call has been activated");
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, DEPRECATED_MESSAGE);
    }

    @Override
    public void bindChildObjectToObject(ObjectId superAppObjectIdBoundary, String superapp, String internalObjectId, String userSuperapp, String email) {
        if (validateUserPermission(userSuperapp, email)) {
            Optional<SuperAppObjectEntity> optionalParentEntity = objects.
                    findById(superAppObjectHelper.getId(superapp, internalObjectId));
            Optional<SuperAppObjectEntity> optionalChildEntity = objects.
                    findById(superAppObjectHelper.getId(superAppObjectIdBoundary.getSuperApp(), superAppObjectIdBoundary.getInternalObjectId()));
            if (optionalParentEntity.isEmpty() && optionalChildEntity.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, SUPER_APP_OBJECT_NOT_FOUND_MSG);
            optionalParentEntity.get().addChildObject(optionalChildEntity.get());
            optionalChildEntity.get().setParentObject(optionalParentEntity.get());
            saveObject(optionalParentEntity.get());
            logger.trace("Parent Object has been saved and binded to child: "+optionalParentEntity.get());
            saveObject(optionalChildEntity.get());
            logger.trace("child Object has been saved and binded to parent: "+optionalChildEntity.get());

        } else {
            logger.error("bindChildObjectToObject Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public List<ObjectBoundary> getChildrenOfObject(String superapp, String internalObjectId, String userSuperapp, String email, int page, int size) {
        if (validateUserPermission(userSuperapp, email)) {
            SuperAppObjectEntity parentObjectEntity = getSuperAppObjectEntity(superapp, internalObjectId);
            logger.trace("getChildrenOfObject Object: "+ parentObjectEntity +" showing his children objects");
            return objects.findAll(PageRequest.of(page, size, Direction.DESC, "type"))
                    .stream()
                    .filter(object -> object.getObjectId().split("_")[1].equals(internalObjectId))
                    .toList().get(0).getChildObjects().stream().map(superAppObjectHelper::buildBoundaryFromEntity)
                    .collect(Collectors.toList());
        } else {
            logger.error("getChildrenOfObject Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public List<ObjectBoundary> getParentsOfObject(String superapp, String internalObjectId, String userSuperapp, String email, int page, int size) {
        if (validateUserPermission(userSuperapp, email)) {
            SuperAppObjectEntity childObjectEntity = getSuperAppObjectEntity(superapp, internalObjectId);
            logger.trace("getParentsOfObject Object: "+ childObjectEntity +" showing his parent object");

            SuperAppObjectEntity[] parentObject = {childObjectEntity.getParentObject()};

            return Arrays.stream(parentObject).map(superAppObjectHelper::buildBoundaryFromEntity).toList();

        } else {
            logger.error("getParentsOfObject Invalid User: "+email+", "+userSuperapp);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }
}
