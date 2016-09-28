package reflectionPattern.test.model;

import reflectionPattern.dataGeneration.*;
import reflectionPattern._deprecated.dataGenerator.RandomFactGenerator;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.operational.CompositeFact;
import reflectionPattern.model.operational.Fact;
import utility.composite.out.CompositeTree;

/**
 * Created by nagash on 09/09/16.
 */
public class ModelGeneratorTest {

    @org.junit.Test
    public void test() throws CompositeFact.IllegalFactTypeException, Range.InfSupValueException, Range.MinimumValueException {


        FactTypeGenerator typeGenerator = new FactTypeGenerator();
        FactTypeGeneratorParam params = typeGenerator.getParam();

        //  Settings for generate only a root element (not Composite):
        //  params.getDepthRange().setInfSup(0, 0);
        //  params.getRootChildsRange().setInfSup(0, 0);

        params.setLeafOnlyAtLowerLevel(false);
        params.getDepthRange().setInfSup(3, 3);
        params.getRootChildsRange().setInfSup(1, 3);
        params.getCompChildsRange().setSup(5);
        params.getPhenomsRange().setSup(4);
        params.getUnitsRange().setSup(4);



        FactType rootType   = typeGenerator.randomFactType();
        Fact rootFact       = RandomFactGenerator.randomFact(rootType);

        CompositeTree.printTree(rootFact);


    }
}
