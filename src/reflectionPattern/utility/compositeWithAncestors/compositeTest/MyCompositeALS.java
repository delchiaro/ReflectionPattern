package reflectionPattern.utility.compositeWithAncestors.compositeTest;


import reflectionPattern.utility.compositeWithAncestors.CompositeManagerALS;
import reflectionPattern.utility.compositeWithAncestors.ICompositeALS;

import java.util.Set;

/**
 * Created by nagash on 17/09/16.
 */
public class MyCompositeALS extends MyComponentALS implements ICompositeALS<MyComponentALS> {

    public final CompositeManagerALS<MyCompositeALS, MyComponentALS> compManager = new CompositeManagerALS<>();


    public MyCompositeALS(String name) {
        super(name);
    }

    @Override
    public void addChild(MyComponentALS child) {
        compManager.addChild(child);
    }

    @Override
    public Set<MyComponentALS> getChilds() {
        return compManager.getChilds();
    }

    //@Override
    public void setChilds(Set<MyComponentALS> childs) {
        compManager.setChilds(childs);
    }
}
