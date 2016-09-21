package reflectionPattern.utility.compositeWithAncestors.test.withSuperclass;

import reflectionPattern.utility.compositeWithAncestors.ComponentManagerALS;
import reflectionPattern.utility.compositeWithAncestors.IComponentALS;

import java.util.List;

/**
 * Created by nagash on 18/09/16.
 */
public class MyComponent
        extends SuperClass
        implements IComponentALS<MyComposite>
{


    ComponentManagerALS<MyComponent, MyComposite> componentManager;

    public MyComponent(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return getName();
    }



    @Override public void addFirstAncestor(MyComposite newAncestor) { componentManager.addFirstAncestor(newAncestor); }
    @Override public void addLastAncestor(MyComposite newAncestor) { componentManager.addLastAncestor(newAncestor); }
    @Override public void appendAllAncestors(List<MyComposite> newAncestors) { componentManager.appendAllAncestors(newAncestors); }
    @Override public List<MyComposite>  getAncestors() { return componentManager.getAncestors(); }

    protected void setAncestors(List<MyComposite> ancestors) {
        componentManager.setAncestors(ancestors);
    }

    @Override public MyComposite getParent() { return componentManager.getParent(); }


//    protected void setParent(MyComposite parent) {
//        componentManager.setParent(parent);
//    }
}
