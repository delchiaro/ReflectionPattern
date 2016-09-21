package reflectionPattern.utility.compositeWithAncestors;

/**
 * Created by nagash on 18/09/16.
 */
public class ComponentManagerALS<CONTAINER extends IComponentALS<COMPOSITE>, COMPOSITE extends ICompositeALS<COMPOSITE, CONTAINER>>
        extends ComponentALS<COMPOSITE>
        // implements IComponentALS<COMPOSITE>
{
    private CONTAINER container;


    protected ComponentManagerALS() {} // for hibernate
    public ComponentManagerALS(CONTAINER container) {
        this.container = container;
    }


    public CONTAINER getContainer(){
        return container;
    }

}
