package utility.compositeWithAncestors.test.noSuperclass;


import utility.compositeWithAncestors.CompositeManagerALS;
import utility.compositeWithAncestors.ICompositeALS;

import java.util.Set;

/**
 * Created by nagash on 17/09/16.
 */
public class MyCompositeALS extends MyComponentALS implements ICompositeALS<MyCompositeALS, MyComponentALS> {

    public final CompositeManagerALS<MyCompositeALS, MyComponentALS> compManager = new CompositeManagerALS<>(this);


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
