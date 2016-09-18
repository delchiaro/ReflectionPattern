package reflectionPattern.utility.compositeWithAncestors.compositeTestWithSuperclass;

import reflectionPattern.utility.compositeWithAncestors.ComponentManager;
import reflectionPattern.utility.compositeWithAncestors.IComponent;

import java.util.List;

/**
 * Created by nagash on 18/09/16.
 */
public class MyComponentWithInheritance extends SuperClass implements IComponent<MyCompositeWithInheritance>
{
    ComponentManager<MyComponentWithInheritance, MyCompositeWithInheritance> componentManager;

    public MyComponentWithInheritance(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return getName();
    }




    @Override public void addFirstAncestor(MyCompositeWithInheritance newAncestor) { componentManager.addFirstAncestor(newAncestor); }
    @Override public void addLastAncestor(MyCompositeWithInheritance newAncestor) { componentManager.addLastAncestor(newAncestor); }
    @Override public void appendAllAncestors(List<MyCompositeWithInheritance> newAncestors) { componentManager.appendAllAncestors(newAncestors); }
    @Override public List<MyCompositeWithInheritance>  getAncestors() { return componentManager.getAncestors(); }
    @Override public MyCompositeWithInheritance getFather() { return componentManager.getFather(); }
}
