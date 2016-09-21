package reflectionPattern.utility.compositeWithAncestors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nagash on 17/09/16.
 */
public class ComponentALS<COMPOSITE extends ICompositeALS> implements IComponentALS<COMPOSITE> {

    // ANCESTOR STRATEGY * * * * * * * * * * * * * * * * * * * *

    private List<COMPOSITE> ancestors = new LinkedList<>();


    public void addFirstAncestor(COMPOSITE newAncestor) {
        ancestors.add(0, newAncestor);
    }

    public void addLastAncestor(COMPOSITE newAncestor) {
        ancestors.add(newAncestor);
    }

    public void appendAllAncestors(List<COMPOSITE> newAncestors) {
        ancestors.addAll(newAncestors);
    }


    public List<COMPOSITE> getAncestors() {
        return Collections.unmodifiableList(ancestors);
    }

    public void setAncestors(List<COMPOSITE> ancestors) {
        this.ancestors = ancestors;
    }

    public COMPOSITE getParent() {
        try { return ancestors.get(0);  }
        catch (IndexOutOfBoundsException e){ return null; }
    }


    public void setParent(COMPOSITE parent) {
        addFirstAncestor(parent);
    }
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *




}
