package reflectionPattern.dataGeneration;

import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Quantity;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.operational.*;

import java.util.Set;

import static reflectionPattern.dataGeneration.RandomUtils.randInt;

/**
 * Created by nagash on 26/09/16.
 */
public class FactGenerator {


    public static void randomFill(Fact fact) {
        RandomFactFiller filler =  new RandomFactFiller();
        fact.acceptVisitor(filler);
    }


    public static Fact generate(FactType type) {
        EmptyFactGenerator efg = new EmptyFactGenerator();
        type.acceptVisitor(efg);
        return efg.getGeneratedFact();
    }


}

class EmptyFactGenerator implements IFactTypeVisitor
{
    Fact rootFact = null;
    Fact generatingFact = null;

    public Fact getGeneratedFact() {
        return generatingFact;
    }
    @Override
    public void visit(Object object) {}

    @Override
    public void visit(FactType type) {}



    @Override
    public void visit(CompositeType type) {
        CompositeFact fact = new CompositeFact(type);
        for(FactType childType : type.getChilds() )
        {
            childType.acceptVisitor(this);
            fact.addChild(generatingFact);
        }
        generatingFact = fact;
    }

    @Override
    public void visit(QualitativeType type) {
        generatingFact = new QualitativeFact(type);
    }

    @Override
    public void visit(QuantitativeType type) {
        generatingFact = new QuantitativeFact(type);
    }

    @Override
    public void visit(TextualType type) {
        generatingFact = new TextualFact(type);
    }

}


class RandomFactFiller implements IFactVisitor
{
    private final static int RANDOM_STRING_LENGTH = 18;



    @Override
    public void visit(Object object) {

    }

    @Override
    public void visit(Fact fact) {

    }

    @Override
    public void visit(CompositeFact fact) {
        for(Fact child : fact.getChilds() )
            child.acceptVisitor(this);
        return;
    }

    @Override
    public void visit(QualitativeFact fact) {
        Set<Phenomenon> legalPhenomenons = ((QualitativeType)fact.getType()).getLegalPhenomenons();
        Phenomenon phenomenon = legalPhenomenons.toArray(new Phenomenon[]{})[ randInt(0, legalPhenomenons.size()-1)];
        fact.setPhenomenon(phenomenon);
        return;
    }

    @Override
    public void visit(QuantitativeFact fact) {
        Set<Unit> legalUnits = ((QuantitativeType)fact.getType()).getLegalUnits();
        Unit unit = legalUnits.toArray(new Unit[]{})[ randInt(0, legalUnits.size()-1)];
        fact.setQuantity(new Quantity(randInt(), unit));
        return;
    }

    @Override
    public void visit(TextualFact fact) {
        fact.setValue(RandomUtils.randomString(RANDOM_STRING_LENGTH));
    }

}

