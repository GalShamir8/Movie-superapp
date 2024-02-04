package superapp.logic;

import org.springframework.web.server.ResponseStatusException;
import superapp.view.ObjectBoundary;
import superapp.models.ObjectId;

import java.util.List;

public interface ObjectsService {

    /**
     * Create new UserEntity in the storage
     *
     * @param object - SuperAppObjectBoundary representative with relevant attributes
     * @return new SuperAppBoundary
     */
    ObjectBoundary createObject(ObjectBoundary object);

    /**
     * Update object by internalObjectId , objectSuperApp
     *
     * @param objectSuperApp   -  relevant str
     * @param internalObjectId - internal id
     * @return updated SuperAppObjectBoundary align with new attributes
     * @throws ResponseStatusException http_status.NOT_FOUND (404) - if the object doesn't exist
     */
    @Deprecated
    ObjectBoundary updateObject(
            String objectSuperApp,
            String internalObjectId,
            ObjectBoundary update
    ) throws ResponseStatusException;


    /**
     * Update user in the system - with corresponding email, super app
     *
     * @param objectSuperApp   -  relevant str
     * @param internalObjectId - internal id
     * @return specific UserBoundary align with new attributes
     * @throws ResponseStatusException http_status.NOT_FOUND (404) - if the object doesn't exist
     */
    @Deprecated
    ObjectBoundary getSpecificObject(
            String objectSuperApp,
            String internalObjectId
    ) throws ResponseStatusException;

    /**
     * @return list of all the Objects
     */

    @Deprecated
    List<ObjectBoundary> getAllObjects();


    /**
     * Delete all the objects in the system
     */
    void deleteAllObjects();


}
