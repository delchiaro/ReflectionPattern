package reflectionPattern.utility.compositeWithAncestors.compositeTestWithSuperclass;

import org.junit.Test;
import reflectionPattern.utility.composite.out.CompositeTree;
import reflectionPattern.utility.compositeWithAncestors.compositeTest.MyComponent;
import reflectionPattern.utility.compositeWithAncestors.compositeTest.MyComposite;

/**
 * Created by nagash on 17/09/16.
 */
public class TestCompositeWithAncestorsAndSuperclass {

    @Test
    public void test()
    {

        MyComposite z = new MyComposite("z");
        MyComposite y = new MyComposite("y");
        MyComposite a = new MyComposite("a");
        MyComposite b = new MyComposite("b");
        MyComponent c = new MyComponent("c");
        MyComposite d = new MyComposite("c");
        MyComponent e = new MyComponent("e");
        MyComponent f = new MyComponent("f");
        MyComposite g = new MyComposite("d");

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



    }
}
