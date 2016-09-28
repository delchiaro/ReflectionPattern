package utility.composite;

/**
 * Created by nagash on 17/09/16.
 */

public class Component<COMPOSITE extends IComposite> implements IComponent<COMPOSITE> {


    private COMPOSITE parent = null;



    public COMPOSITE getParent() {
        return parent;
    }


    public void setParent(COMPOSITE parent) {
        this.parent = parent;
    }


}
