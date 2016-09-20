package reflectionPattern.utility.composite;

/**
 * Created by nagash on 18/09/16.
 */


public class ComponentManager<CONTAINER extends IComponent<COMPOSITE>, COMPOSITE extends IComposite>
        extends Component<COMPOSITE>
        implements IComponent<COMPOSITE>
{
    private CONTAINER container;

    protected ComponentManager() {} // for hibernate
    public ComponentManager(CONTAINER container) {
        this.container = container;
    }

    public CONTAINER getContainer(){
        return container;
    }


}
