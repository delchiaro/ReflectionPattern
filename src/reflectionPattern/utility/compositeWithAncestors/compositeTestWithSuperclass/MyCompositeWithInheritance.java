package reflectionPattern.utility.compositeWithAncestors.compositeTestWithSuperclass;


import reflectionPattern.utility.compositeWithAncestors.CompositeManager;
import reflectionPattern.utility.compositeWithAncestors.IComposite;
import reflectionPattern.utility.compositeWithAncestors.compositeTest.MyComponent;

import java.util.Set;

/**
 * Created by nagash on 17/09/16.
 */
public class MyCompositeWithInheritance extends MyComponentWithInheritance implements IComposite<MyComponent> {

    public final CompositeManager<MyCompositeWithInheritance, MyComponent> compManager = new CompositeManager<>();


    public MyCompositeWithInheritance(String name) {
        super(name);
    }

    @Override
    public void addChild(MyComponent child) {
        compManager.addChild(child);
    }

    @Override
    public Set<MyComponent> getChilds() {
        return compManager.getChilds();
    }
}
