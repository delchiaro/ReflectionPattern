package reflectionPattern.utility.composite;

import java.util.*;

/**
 * Created by nagash on 17/09/16.
 */


public class CompositeManager<CONTAINER extends IComposite<COMPONENT>,  COMPONENT extends IComponent>
        extends Component
        implements IComposite<COMPONENT>

{

    private CONTAINER container;
    public CONTAINER getContainer(){
        return container;
    }


    public CompositeManager(CONTAINER container) {
        this.container = container;
    }

    private Set<COMPONENT> childs = new HashSet<>();


    @Override public void addChild(COMPONENT child) {
        child.setParent(this.getContainer(), new CompositeManagerToken());
        this.childs.add(child);
    }

    @Override public Set<COMPONENT> getChilds() {
        return Collections.unmodifiableSet(childs);
    }

    public void setChilds(Set<COMPONENT> childs) {
        this.childs = childs;
    }






    public static class CompositeManagerToken {
        /**
         *  Only the CompositeManager can create this token and call methods
         *  that require an instance of this token (Friend Methods with Java)
         */
        private CompositeManagerToken() {}
    }


}
