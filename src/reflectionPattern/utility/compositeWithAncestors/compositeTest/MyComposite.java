package reflectionPattern.utility.compositeWithAncestors.compositeTest;


import reflectionPattern.utility.compositeWithAncestors.CompositeManager;
import reflectionPattern.utility.compositeWithAncestors.IComposite;

import java.util.Set;

/**
 * Created by nagash on 17/09/16.
 */
public class MyComposite extends MyComponent implements IComposite<MyComponent> {

    public final CompositeManager<MyComposite, MyComponent> compManager = new CompositeManager<>();


    public MyComposite(String name) {
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
