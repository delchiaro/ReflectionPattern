package reflectionPattern.utility.compositeWithAncestors.compositeTestWithSuperclass;

import org.junit.Test;
import reflectionPattern.utility.composite.out.CompositeTree;
import reflectionPattern.utility.compositeWithAncestors.compositeTest.MyComponentALS;
import reflectionPattern.utility.compositeWithAncestors.compositeTest.MyCompositeALS;
import reflectionPattern.utility.compositeWithAncestors.out.CompositeTreeALS;

/**
 * Created by nagash on 17/09/16.
 */
public class TestCompositeWithAncestorsAndSuperclass {

    @Test
    public void test()
    {

        MyCompositeALS z = new MyCompositeALS("z");
        MyCompositeALS y = new MyCompositeALS("y");
        MyCompositeALS a = new MyCompositeALS("a");
        MyCompositeALS b = new MyCompositeALS("b");
        MyComponentALS c = new MyComponentALS("c");
        MyCompositeALS d = new MyCompositeALS("c");
        MyComponentALS e = new MyComponentALS("e");
        MyComponentALS f = new MyComponentALS("f");
        MyCompositeALS g = new MyCompositeALS("d");

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



    }
}
