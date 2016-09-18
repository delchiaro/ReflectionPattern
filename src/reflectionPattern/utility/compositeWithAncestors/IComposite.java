package reflectionPattern.utility.compositeWithAncestors;

import java.util.Set;

/**
 * Created by nagash on 18/09/16.
 */
public interface IComposite<COMPONENT extends IComponent> extends reflectionPattern.utility.composite.IComposite<COMPONENT> {
    @Override public void addChild(COMPONENT child);
    @Override public Set<COMPONENT> getChilds();
}
