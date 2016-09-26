package reflectionPattern.dataGeneration.deprecated;

import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.operational.*;
import java.util.Set;

import static reflectionPattern.dataGeneration.RandomUtils.randInt;
import static reflectionPattern.dataGeneration.RandomUtils.randomString;

/**
 * Created by nagash on 12/09/16.
 */
@Deprecated
public class RandomFactGenerator {

    public static Fact randomFact(FactType rootType)
    {
        Fact fact = null;
        if(rootType instanceof CompositeType)
        {
            CompositeFact compFact;
            fact = compFact = new CompositeFact((CompositeType) rootType);

            for (FactType childType : ((CompositeType) rootType).getChilds())
            {
                compFact.addChild(randomFact(childType));
            }
        }

        else
        {
            if(rootType instanceof QualitativeType)
            {
                QualitativeType qType = (QualitativeType) rootType;
                Set<Phenomenon> phens = qType.getLegalPhenomenons();

                Phenomenon myPhen = phens.toArray(new Phenomenon[]{})[randInt(0, phens.size()-1)];
                try {
                    fact = new QualitativeFact(qType, myPhen );
                }
                catch (QualitativeFact.IllegalQualitativePhenomenonException e) {
                    e.printStackTrace();
                }
            }
            else if(rootType instanceof QuantitativeType)
            {
                QuantitativeType qType = (QuantitativeType)rootType;
                Set<Unit> units =  qType.getLegalUnits();

                Unit myUnit = units.toArray(new Unit[]{})[randInt(0, units.size()-1)];
                int myRandomValue = randInt(0, 1000);
                try {
                    fact = new QuantitativeFact(qType, myRandomValue, myUnit);
                }
                catch (QuantitativeFact.IllegalQuantitativeUnitException e) {
                    e.printStackTrace();
                }
            }
            else if(rootType instanceof TextualType )
            {
                TextualType tType = (TextualType) rootType;
                int randomLen = randInt(0, 100);
                fact = new TextualFact(tType, randomString(randomLen));
            }
            else return null; //TODO: throws exception? no..
        }
        return fact;
    }

}
