package superapp.logic;

import superapp.models.ObjectId;
import superapp.view.ObjectBoundary;

import java.util.List;

public interface ObjectsPageableSearchableService extends AdvancedObjectsService {
    /**
     * Search super app object by its type
     * @param type superapp object type
     * @param userSuperapp user super app
     * @param email user email
     * @param page number (starting at 0 offset)
     * @param size per page
     * @return list of super app object with the given type paginate
     */
    List<ObjectBoundary> searchByType(String type, String userSuperapp, String email, int page, int size);

    /**
     * Search super app object by its type
     * @param alias superapp object alias
     * @param userSuperapp user super app
     * @param email user email
     * @param page number (starting at 0 offset)
     * @param size per page
     * @return list of super app object with the given alias paginate
     */
    List<ObjectBoundary> searchByAlias(String alias, String userSuperapp, String email, int page, int size);

    /**
     * Search super app object by its type
     * @param alias superapp object alias
     * @param userSuperapp user super app
     * @param email user email
     * @param page number (starting at 0 offset)
     * @param size per page
     * @return list of super app object contains given alias paginate
     */
    List<ObjectBoundary> searchByAliasLike(String alias, String userSuperapp, String email, int page, int size);

    /**
     * Search super app object by its type
     * @param creationEnum superapp object createdAt timestamp string representing
     * @param userSuperapp user super app
     * @param email user email
     * @param page number (starting at 0 offset)
     * @param size per page
     * @return list of super app object with the given createdAt timestamp paginate
     */
    List<ObjectBoundary> searchByCreatedAt(String creationEnum, String userSuperapp, String email, int page, int size);

    /**
     * update object with given internalObjectId
     * @param objBoundary
     * @param superapp
     * @param internalObjectId
     * @param userSuperapp user super app
     * @param email user email
     */
    void updateObject(ObjectBoundary objBoundary, String superapp, String internalObjectId, String userSuperapp, String email);

    /**
     * Get an object corresponding given internalObjectId
     * @param superapp
     * @param internalObjectId
     * @param userSuperapp user super app
     * @param email user email
     * @return Object corresponding given internalObjectId
     */
    ObjectBoundary getSpecificObject(String superapp, String internalObjectId, String userSuperapp, String email);


    /**
     *
     * @param userSuperapp
     * @param email user email
     * @param page number (starting at 0 offset)
     * @param size per page
     * @return all object paginate
     */
    List<ObjectBoundary> getAllObjects(String userSuperapp, String email, int page, int size);

    /**
     *
     * @param superAppObjectIdBoundary - As JSON
     * @param superapp
     * @param internalObjectId
     * @param userSuperapp - the super app username
     * @param email - the user's email
     *              Binds an object to it's child
     */
    void bindChildObjectToObject(ObjectId superAppObjectIdBoundary, String superapp, String internalObjectId, String userSuperapp, String email);

    /**
     *
     * @param superapp
     * @param internalObjectId
     * @param userSuperapp - the super app username
     * @param email - the user's email
     * @param page - int for pagination - the page number to view
     * @param size - the size of a batch to view per page
     * @return a List of ObjectBoundaries of children of a certain object
     */
    List<ObjectBoundary> getChildrenOfObject(String superapp, String internalObjectId,String userSuperapp, String email, int page, int size);

    /**
     *
     * @param superapp
     * @param internalObjectId
     * @param userSuperapp - the super app username
     * @param email - the user's email
     * @param page - int for pagination - the page number to view
     * @param size - the size of a batch to view per page
     * @return a List of ObjectBoundaries of parents of a certain object
     */
    List<ObjectBoundary> getParentsOfObject(String superapp, String internalObjectId,String userSuperapp, String email, int page, int size);
}
