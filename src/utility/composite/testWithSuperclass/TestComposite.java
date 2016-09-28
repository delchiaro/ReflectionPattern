package utility.composite.testWithSuperclass;

import org.junit.Test;
import utility.composite.IComponent;
import utility.composite.IComposite;
import utility.composite.out.CompositeTree;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

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


        assertTrue(testParent(g, d));
        assertTrue(testParent(f, d));

        assertTrue(testParent(d, b));
        assertTrue(testParent(e, b));

        assertTrue(testParent(c, a));
        assertTrue(testParent(b, a));

        assertTrue(testParent(a, z));
        assertTrue(testParent(z, y));
        assertTrue(testParent(y, null));



        assertFalse(testParent(g, g));
        assertFalse(testParent(g, null));
        assertFalse(testParent(g, z));
        assertFalse(testParent(g, y));
        assertFalse(testParent(g, b));

        assertFalse(testParent(a, null));
        assertFalse(testParent(a, b));
    }

    private boolean testParent(IComponent child, IComposite testParent)
    {
       if(child.getParent() == testParent)
           return true;

       else return false;
    }
}
