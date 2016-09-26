package reflectionPattern.utility.compositeWithAncestors;

import reflectionPattern.utility.composite.IComposite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nagash on 17/09/16.
 */
public class ComponentALS<COMPOSITE extends ICompositeALS> implements IComponentALS<COMPOSITE> {

    // ANCESTOR STRATEGY * * * * * * * * * * * * * * * * * * * *


    private COMPOSITE parent;
    private List<COMPOSITE> ancestors = new LinkedList<COMPOSITE>();


    public void addFirstAncestor(COMPOSITE newAncestor) {
        if(newAncestor!=null)
            ancestors.add(0, newAncestor);
        // TODO: set newAncestor as father? Stop to think to the ancestors list as an ordered list? (merge addFirst/addLast?)
    }

    public void addLastAncestor(COMPOSITE newAncestor) {
        ancestors.add(newAncestor);
    }

    public void appendAllAncestors(List<COMPOSITE> newAncestors) {
        ancestors.addAll(newAncestors);
    }



    public List<COMPOSITE> getAncestors() {
        //return Collections.unmodifiableList(ancestors); // hibernate needs a modifiable collection
        return ancestors;
    }

    public void setAncestors(List<COMPOSITE> ancestors) {
        this.ancestors = ancestors;
    }

    public COMPOSITE getParent() {
        return parent;
    }


    /** TODO: change this comment?
     * Parent is a ReadOnly FIELD!! Set the hibernate association as "updatable = false" for the Parent getter/setter!!
     * This setter is fake, use addFirstAncestor(..) instead
     * @param parent
     */
    public void setParent(COMPOSITE parent) {
        this.parent = parent; // added parent property
        //TODO: check if parent is in the ancestors list or not? (hibernate performance?)
    }
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *




}
