package utility.composite.testWithSuperclass;


import utility.composite.CompositeManager;
import utility.composite.IComposite;

import java.util.Set;

/**
 * Created by nagash on 17/09/16.
 */
public class MyComposite
        extends MyComponent
        implements IComposite<MyComposite,MyComponent>

{

    public final CompositeManager<MyComposite, MyComponent> compositeManager = new CompositeManager<>(this);


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
