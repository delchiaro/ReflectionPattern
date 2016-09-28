package utility.compositeWithAncestors;

import utility.composite.IComponent;

import java.util.List;

/**
 * Created by nagash on 18/09/16.
 */
public interface IComponentALS<COMPOSITE extends ICompositeALS> extends IComponent<COMPOSITE>  {

    public void addFirstAncestor(COMPOSITE newAncestor);
    public void addLastAncestor(COMPOSITE newAncestor);
    public void appendAllAncestors(List<COMPOSITE> newAncestors);


    public List<COMPOSITE> getAncestors();
    //public void setAncestors(List<COMPOSITE> ancestors);

    public COMPOSITE getParent();
    public void setParent(COMPOSITE parent);
}
