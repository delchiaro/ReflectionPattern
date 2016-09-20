package reflectionPattern.utility.composite.compositeTest;

import org.junit.Test;
import reflectionPattern.utility.composite.CompositeManager;
import reflectionPattern.utility.composite.out.CompositeTree;

/**
 * Created by nagash on 17/09/16.
 */
public class TestComposite {

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

        d.compositeManager.addChild(f);
        d.compositeManager.addChild(g);

        a.compositeManager.addChild(c);
        a.compositeManager.addChild(b);

        b.compositeManager.addChild(e);
        b.compositeManager.addChild(d);

        z.compositeManager.addChild(a);
        y.compositeManager.addChild(z);


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
