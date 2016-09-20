package reflectionPattern.utility.composite.compositeTest;

import reflectionPattern.utility.composite.ComponentManager;
import reflectionPattern.utility.composite.CompositeManager;
import reflectionPattern.utility.composite.IComponent;
import reflectionPattern.utility.compositeWithAncestors.ComponentManagerALS;
import reflectionPattern.utility.compositeWithAncestors.IComponentALS;

import java.util.List;

/**
 * Created by nagash on 18/09/16.
 */
public class MyComponent
        extends SuperClass
        implements IComponent<MyComposite> {


    ComponentManager<MyComponent, MyComposite> componentManager = new ComponentManager<>(this);

    public MyComponent(String name) {
        super(name);
    }


    @Override
    public MyComposite getParent() {
        return componentManager.getParent();
    }

    @Override
    public void setParent(MyComposite parent, CompositeManager.CompositeManagerToken friendToken) {
        componentManager.setParent(parent);
    }


//    protected void setParent(MyComposite parent) {
//        componentManager.setParent(parent);
//    }



    @Override
    public String toString() {
        return getName();
    }

}
