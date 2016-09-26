package reflectionPattern.dataGeneration;

import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Quantity;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.operational.*;

import java.util.Set;

import static reflectionPattern.dataGeneration.RandomUtils.randInt;
import static reflectionPattern.dataGeneration.RandomUtils.randomString;

/**
 * Created by nagash on 26/09/16.
 */
public class FactGenerator {
    private final static int RANDOM_STRING_LENGTH = 18;

    public static Fact generate(FactType type) {
        return null; // Fact is abstract!
    }
    public static CompositeFact generate(CompositeType type) {
        CompositeFact fact = new CompositeFact(type);
        for(FactType childType : type.getChilds() )
            fact.addChild(generate(childType));
        return fact;
    }
    public static QualitativeFact generate(QualitativeType type) {
        QualitativeFact fact = new QualitativeFact(type);
        return fact;
    }
    public static QuantitativeFact generate(QuantitativeType type) {
        QuantitativeFact fact = new QuantitativeFact(type);
        return fact;
    }
    public static TextualFact generate(TextualType type) {
        TextualFact fact = new TextualFact(type);
        return fact;
    }





    public static void randomFill(Fact fact) {
        return;
    }
    public static void randomFill(CompositeFact fact) {
        for(Fact child : fact.getChilds() )
            randomFill(child);
        return;
    }
    public static void randomFill(QualitativeFact fact) {
        Set<Phenomenon> legalPhenomenons = ((QualitativeType)fact.getType()).getLegalPhenomenons();
        Phenomenon phenomenon = legalPhenomenons.toArray(new Phenomenon[]{})[ randInt(0, legalPhenomenons.size()-1)];
        fact.setPhenomenon(phenomenon);
        return;
    }
    public static void randomFill(QuantitativeFact fact) {
        Set<Unit> legalUnits = ((QuantitativeType)fact.getType()).getLegalUnits();
        Unit unit = legalUnits.toArray(new Unit[]{})[ randInt(0, legalUnits.size()-1)];
        fact.setQuantity(new Quantity(randInt(), unit));
        return;
    }
    public static void randomFill(TextualFact fact) {
        fact.setValue(RandomUtils.randomString(RANDOM_STRING_LENGTH));
        return;
    }






}
