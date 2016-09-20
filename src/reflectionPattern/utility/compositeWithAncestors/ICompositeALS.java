package reflectionPattern.utility.compositeWithAncestors;

import reflectionPattern.utility.composite.IComposite;

import java.util.Set;

/**
 * Created by nagash on 18/09/16.
 */
public interface ICompositeALS<COMPONENT extends IComponentALS>  {
    public void addChild(COMPONENT child);
    public Set<COMPONENT> getChilds();

    //public void setChilds(Set<COMPONENT> childs);
}
