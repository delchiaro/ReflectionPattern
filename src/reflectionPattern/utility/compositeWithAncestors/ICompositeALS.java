package reflectionPattern.utility.compositeWithAncestors;

import java.util.Set;

/**
 * Created by nagash on 18/09/16.
 */
public interface ICompositeALS<COMPOSITE extends ICompositeALS, COMPONENT extends IComponentALS> extends  IComponentALS<COMPOSITE>{
    public void addChild(COMPONENT child);
    public Set<COMPONENT> getChilds();

    //public void setChilds(Set<COMPONENT> childs);
}
