package reflectionPattern.utility.compositeWithAncestors;

import java.util.List;

/**
 * Created by nagash on 18/09/16.
 */
public interface IComponent<COMPOSITE extends IComposite> extends reflectionPattern.utility.composite.IComponent<COMPOSITE> {

    public void addFirstAncestor(COMPOSITE newAncestor);

    public void addLastAncestor(COMPOSITE newAncestor);

    public void appendAllAncestors(List<COMPOSITE> newAncestors);


    public List<COMPOSITE> getAncestors();

    @Override
    public COMPOSITE getFather();
}
