package utility.visitor;

/**
 * Created by nagash on 27/09/16.
 */
public interface Visitable<T extends IVisitor> {

    public void acceptVisitor(T visitor);
}
