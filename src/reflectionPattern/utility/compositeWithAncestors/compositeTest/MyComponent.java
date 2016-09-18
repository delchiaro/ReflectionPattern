package reflectionPattern.utility.compositeWithAncestors.compositeTest;
import reflectionPattern.utility.compositeWithAncestors.Component;

/**
 * Created by nagash on 17/09/16.
 */
public class MyComponent extends Component<MyComposite> {
    String name;

    public MyComponent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
