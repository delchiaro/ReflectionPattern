package reflectionPattern.utility.composite;

/**
 * Created by nagash on 17/09/16.
 */
public class Component<COMPOSITE extends IComposite> implements IComponent<COMPOSITE> {

    private COMPOSITE father = null;


    void setFather(COMPOSITE father) {
        this.father = father;
    }

    @Override
    public COMPOSITE getFather() {
        return father;
    }
}
