package reflectionPattern.utility.compositeWithAncestors.test.withSuperclass;

import reflectionPattern.utility.composite.out.CompositeTree;
import reflectionPattern.utility.compositeWithAncestors.IComponentALS;
import reflectionPattern.utility.compositeWithAncestors.ICompositeALS;
import reflectionPattern.utility.compositeWithAncestors.test.noSuperclass.MyComponentALS;
import reflectionPattern.utility.compositeWithAncestors.test.noSuperclass.MyCompositeALS;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by nagash on 17/09/16.
 */
public class TestCompositeALS_superclass {

    @org.junit.Test
    public void testAncestors()
    {

        MyCompositeALS z = new MyCompositeALS("z");
        MyCompositeALS y = new MyCompositeALS("y");
        MyCompositeALS a = new MyCompositeALS("a");
        MyCompositeALS b = new MyCompositeALS("b");
        MyComponentALS c = new MyComponentALS("c");
        MyCompositeALS d = new MyCompositeALS("d");
        MyComponentALS e = new MyComponentALS("e");
        MyComponentALS f = new MyComponentALS("f");
        MyCompositeALS g = new MyCompositeALS("g");

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



        assertFalse( testAncestors(g, new ICompositeALS[] {b, d, a, z, y } ) );
        assertFalse( testAncestors(g, new ICompositeALS[] {d, b, a, z } ) );
        assertFalse( testAncestors(g, new ICompositeALS[] {d, b, a, y, z } ) );
        assertFalse( testAncestors(g, new ICompositeALS[] {d, b, a, a, y } ) );
        assertFalse( testAncestors(g, new ICompositeALS[] {d, b, z, a, y } ) );


        assertTrue ( testAncestors(g, new ICompositeALS[] {d, b, a, z, y } ) );
        assertTrue ( testAncestors(f, new ICompositeALS[] {d, b, a, z, y } ) );

        assertTrue ( testAncestors(d, new ICompositeALS[] { b, a, z, y } ) );
        assertTrue ( testAncestors(e, new ICompositeALS[] { b, a, z, y } ) );

        assertTrue ( testAncestors(b, new ICompositeALS[] { a, z, y } ) );
        assertTrue ( testAncestors(c, new ICompositeALS[] { a, z, y } ) );

        assertTrue ( testAncestors(a, new ICompositeALS[] { z, y } ) );
        assertTrue ( testAncestors(z, new ICompositeALS[] { y } ) );
        assertTrue ( testAncestors(y, new ICompositeALS[] { } ) );

        assertFalse ( testAncestors(y, new ICompositeALS[] { z } ) );
        assertFalse ( testAncestors(y, new ICompositeALS[] { null } ) );
        assertFalse ( testAncestors(y, new ICompositeALS[] { y } ) );




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

    private boolean testAncestors(IComponentALS child, ICompositeALS[] equalList)
    {
        List<? extends ICompositeALS> ancestors = child.getAncestors();

        if(ancestors.size() != equalList.length)
            return false;

        int index = 0;
        for (ICompositeALS anc : ancestors)
        {
            if(anc != equalList[index])
                return false;
            index++;
        }

        return true;
    }



    private boolean testParent(IComponentALS child, ICompositeALS testParent)
    {
        if(child.getParent() == testParent)
            return true;

        else return false;
    }
}
