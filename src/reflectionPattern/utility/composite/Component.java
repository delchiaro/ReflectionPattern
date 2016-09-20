package reflectionPattern.utility.composite;

/**
 * Created by nagash on 17/09/16.
 */

public class Component<COMPOSITE extends IComposite> implements IComponent<COMPOSITE> {


    private COMPOSITE parent = null;



    public COMPOSITE getParent() {
        return parent;
    }

    @Override
    public void setParent(COMPOSITE parent, CompositeManager.CompositeManagerToken friendToken) {
        this.parent = parent;
    }

    public void setParent(COMPOSITE parent) {
        this.parent = parent;
    }


}
