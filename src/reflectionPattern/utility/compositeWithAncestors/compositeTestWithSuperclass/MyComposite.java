package reflectionPattern.utility.compositeWithAncestors.compositeTestWithSuperclass;


import reflectionPattern.utility.compositeWithAncestors.CompositeManagerALS;
import reflectionPattern.utility.compositeWithAncestors.ICompositeALS;

import java.util.Set;

/**
 * Created by nagash on 17/09/16.
 */
public class MyComposite
        extends MyComponent
        implements ICompositeALS<MyComponent>
{

    public final CompositeManagerALS<MyComposite, MyComponent> compositeManager = new CompositeManagerALS<>();


    public MyComposite(String name) {
        super(name);
    }


    @Override
    public void addChild(MyComponent child) {
        compositeManager.addChild(child);
    }

    @Override
    public Set<MyComponent> getChilds() {
        return compositeManager.getChilds();
    }

}
