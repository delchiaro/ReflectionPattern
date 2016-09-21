package reflectionPattern.utility.compositeWithAncestors.test.noSuperclass;

import org.junit.Test;
import reflectionPattern.utility.compositeWithAncestors.IComponentALS;
import reflectionPattern.utility.compositeWithAncestors.ICompositeALS;
import reflectionPattern.utility.compositeWithAncestors.out.CompositeTreeALS;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by nagash on 17/09/16.
 */
public class TestCompositeALS {

    @Test
    public void test()
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

        // g.ancestors.get(0)
        //                  d.getancestors.get(0) == b
        //                  d.getancestors.get(1) == b
        //
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
        CompositeTreeALS.printTree(y);


        assertFalse( assertAncestors(g, new ICompositeALS[] {b, d, a, z, y } ) );
        assertFalse( assertAncestors(g, new ICompositeALS[] {d, b, a, z } ) );
        assertFalse( assertAncestors(g, new ICompositeALS[] {d, b, a, y, z } ) );
        assertFalse( assertAncestors(g, new ICompositeALS[] {d, b, a, a, y } ) );
        assertFalse( assertAncestors(g, new ICompositeALS[] {d, b, z, a, y } ) );


        assertTrue ( assertAncestors(g, new ICompositeALS[] {d, b, a, z, y } ) );
        assertTrue ( assertAncestors(f, new ICompositeALS[] {d, b, a, z, y } ) );

        assertTrue ( assertAncestors(d, new ICompositeALS[] { b, a, z, y } ) );
        assertTrue ( assertAncestors(e, new ICompositeALS[] { b, a, z, y } ) );

        assertTrue ( assertAncestors(b, new ICompositeALS[] { a, z, y } ) );
        assertTrue ( assertAncestors(c, new ICompositeALS[] { a, z, y } ) );

        assertTrue ( assertAncestors(a, new ICompositeALS[] { z, y } ) );
        assertTrue ( assertAncestors(z, new ICompositeALS[] { y } ) );
        assertTrue ( assertAncestors(y, new ICompositeALS[] { } ) );

        assertFalse ( assertAncestors(y, new ICompositeALS[] { z } ) );
        assertFalse ( assertAncestors(y, new ICompositeALS[] { null } ) );
        assertFalse ( assertAncestors(y, new ICompositeALS[] { y } ) );



    }

    private boolean assertAncestors(IComponentALS child, ICompositeALS[] equalList)
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



}
