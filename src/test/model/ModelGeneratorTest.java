package test.model;

import reflectionPattern.IO.OutputManager;
import reflectionPattern.IO.compositeAdapter.FactCompositeAdapter;
import reflectionPattern.dataGeneration.*;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import reflectionPattern.utility.composite.out.CompositeTree;
import reflectionPattern.utility.compositeWithAncestors.out.CompositeTreeALS;

/**
 * Created by nagash on 09/09/16.
 */
public class ModelGeneratorTest {

    @org.junit.Test
    public void test() throws CompositeFact.IllegalFactTypeException, Range.InfSupValueException, Range.MinimumValueException {


        FactTypeGenerator typeGenerator = new FactTypeGenerator();
        FactTypeGeneratorParam params = typeGenerator.getParam();

        //  Settings for generate only a root element (not Composite):
        //  params.depthRange().setInfSup(0, 0);
        //  params.rootChildsRange().setInfSup(0, 0);

        params.setLeafOnlyAtLowerLevel(false);
        params.depthRange().setInfSup(3, 3);
        params.rootChildsRange().setInfSup(1, 3);
        params.compChildsRange().setSup(5);
        params.phenomsRange().setSup(4);
        params.unitsRange().setSup(4);



        FactType rootType   = typeGenerator.randomFactType();
        Fact rootFact       = FactGenerator.randomFact(rootType);

        CompositeTree.printTree(rootFact);


    }
}
