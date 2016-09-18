package reflectionPattern.utility.composite;

/**
 * Created by nagash on 18/09/16.
 */
public interface IComponent<COMPOSITE extends IComposite> {
    public COMPOSITE getFather();


}
