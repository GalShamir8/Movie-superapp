package superapp.logic;

import superapp.models.ObjectId;
import superapp.view.ObjectBoundary;

import java.util.List;

public interface AdvancedObjectsService extends ObjectsService {

    /**
     * @param superAppObjectIdBoundary - as a JSON
     * @param superapp
     * @param internalObjectId
     */
    @Deprecated
    void bindChildObjectToObject(ObjectId superAppObjectIdBoundary, String superapp, String internalObjectId);

    /**
     * @param superapp
     * @param internalObjectId
     * @return a List of ObjectBoundaries of children of a certain object
     */
    @Deprecated
    List<ObjectBoundary> getChildrenOfObject(String superapp, String internalObjectId);

    /**
     * @param superapp
     * @param internalObjectId
     * @return a List of ObjectBoundaries of parents of a certain object
     */
    @Deprecated
    List<ObjectBoundary> getParentsOfObject(String superapp, String internalObjectId);

}
