package reflectionPattern.utility.compositeWithAncestors.compositeTest;
import reflectionPattern.utility.compositeWithAncestors.ComponentALS;

/**
 * Created by nagash on 17/09/16.
 */
public class MyComponentALS extends ComponentALS<MyCompositeALS> {
    String name;

    public MyComponentALS(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
