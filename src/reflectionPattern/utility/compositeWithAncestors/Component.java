package reflectionPattern.utility.compositeWithAncestors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nagash on 17/09/16.
 */
public class Component<COMPOSITE extends IComposite> implements IComponent<COMPOSITE> {

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

    public COMPOSITE getFather() {
        return ancestors.get(0);
    }
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


}
