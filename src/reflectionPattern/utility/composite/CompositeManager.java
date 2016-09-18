package reflectionPattern.utility.composite;

import java.util.*;

/**
 * Created by nagash on 17/09/16.
 */

public class CompositeManager<COMPONENT extends Component>  extends Component implements IComposite<COMPONENT>

{


    private Set<COMPONENT> childs = new HashSet<>();


    public void addChild(COMPONENT child) {
        child.setFather(this);
        this.childs.add(child);
    }

    public Set<COMPONENT> getChilds() {
        return Collections.unmodifiableSet(childs);
    }





}
