package reflectionPattern.utility.composite;

/**
 * Created by nagash on 18/09/16.
 */
public interface IComponent<COMPOSITE extends IComposite> {
    public COMPOSITE getParent();
    public void setParent(COMPOSITE parent, CompositeManager.CompositeManagerToken friendToken);





//    public Private<COMPOSITE> getPrivateMethods(IComponent.Token token);
//
//    interface Private<COMPOSITE extends IComposite>  {
//        public void setParent(COMPOSITE parent);
//    }



}
