package utility.composite.testWithSuperclass;

import utility.composite.ComponentManager;
import utility.composite.IComponent;

/**
 * Created by nagash on 18/09/16.
 */
public class MyComponent
        extends SuperClass
        implements IComponent<MyComposite>
{


    ComponentManager<MyComponent, MyComposite> componentManager = new ComponentManager<>(this);

    public MyComponent(String name) {
        super(name);
    }


    @Override public MyComposite getParent() {
        return componentManager.getParent();
    }

    @Override public void setParent(MyComposite parent) {
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
