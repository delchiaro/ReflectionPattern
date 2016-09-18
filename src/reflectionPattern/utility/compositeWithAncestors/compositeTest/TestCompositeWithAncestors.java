package reflectionPattern.utility.compositeWithAncestors.compositeTest;

import org.junit.Test;
import reflectionPattern.utility.composite.out.CompositeTree;

/**
 * Created by nagash on 17/09/16.
 */
public class TestCompositeWithAncestors {

    @Test
    public void test()
    {

        MyComposite z = new MyComposite("z");
        MyComposite y = new MyComposite("y");
        MyComposite a = new MyComposite("a");
        MyComposite b = new MyComposite("b");
        MyComponent c = new MyComponent("c");
        MyComposite d = new MyComposite("d");
        MyComponent e = new MyComponent("e");
        MyComponent f = new MyComponent("f");
        MyComposite g = new MyComposite("g");

        d.compManager.addChild(f);
        d.compManager.addChild(g);

        a.compManager.addChild(c);
        a.compManager.addChild(b);

        b.compManager.addChild(e);
        b.compManager.addChild(d);

        z.compManager.addChild(a);
        y.compManager.addChild(z);

        /**
         *
         * y
         * |-z
         *   |--a
         *      |-- c
         *      |
         *      |-- b
         *          |-- e
         *          |
         *          |--d
         *             |-- f
         *             |
         *             |-- g
         *
         **/
        CompositeTree.printTree(y);

        // TODO: ancestors test

    }
}
