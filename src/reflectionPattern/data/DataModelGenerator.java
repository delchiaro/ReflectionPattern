package reflection.data;

import org.terracotta.statistics.jsr166e.ThreadLocalRandom;
import reflection.model.knowledge.*;
import reflection.model.knowledge.quantity.Unit;
import reflection.model.operational.*;

import java.security.SecureRandom;
import java.util.Set;

/**
 * Created by nagash on 07/09/16.
 */
public class DataModelGenerator {



    public class UnknownFactTypeException extends  Exception{}

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    private static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private static int randInt(int min, int max) {
        if(min==max) return min;
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }





    public Fact randomFactGenerator(FactType rootType) throws UnknownFactTypeException, CompositeFact.IllegalFactTypeException {

        Fact fact = null;
        if(rootType instanceof  CompositeType)
        {
            CompositeFact compFact;
            fact = compFact = new CompositeFact((CompositeType) rootType);

            for (FactType childType : ((CompositeType) rootType).getChildTypes())
                compFact.addChild(randomFactGenerator(childType));
        }

        else
        {
            if(rootType instanceof QualitativeType )
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
            else if(rootType instanceof QuantitativeType )
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
            else throw new UnknownFactTypeException();
        }


        return fact;

    }



    private Fact pseudoRandomFactGenerator(FactType type) throws UnknownFactTypeException {
        Fact fact = null;
        if(type instanceof  CompositeType)
        {
            return new CompositeFact((CompositeType) type);
        }

        return fact;

    }









    public long timeMillis(){
        return (System.currentTimeMillis()); // % 1000);
    }



    /**
     *
     * @param forceFirstMinWidth if >= 1, force the root of FactType to be a CompositeType with at least 'forceFirstMinWidth' childs
     * @param maxDepth max depth of the FactType tree (max depth of recursion)
     * @param maxWidth max number of child for each CompositeType (minimum is fixed to 1)
     * @param maxPhenoms max number of phenomenos for each Qualitative Type (minimum is fixed to 1)
     * @param maxUnits max number of Measurement Units for each Quantitative Type (minimum is fixed to 1)
     * @return
     * @throws UnknownFactTypeException
     */
    public FactType randomKnowledgeGenerator(int forceFirstMinWidth, int maxDepth, int maxWidth, int maxPhenoms, int maxUnits) throws UnknownFactTypeException {

        // TODO: data validation
        return randomKnowledgeGenerator(forceFirstMinWidth, 1, maxDepth, maxWidth, maxPhenoms, maxUnits);
    }

    /**
     *
     * @param forceFirstMinWidth if >= 1, force the root of FactType to be a CompositeType with at least 'forceFirstMinWidth' childs
     * @param depth recursion index.
     * @param maxDepth max depth of the FactType tree (max depth of recursion)
     * @param maxWidth max number of child for each CompositeType (minimum is fixed to 1)
     * @param maxPhenoms max number of phenomenos for each Qualitative Type (minimum is fixed to 1)
     * @param maxUnits max number of Measurement Units for each Quantitative Type (minimum is fixed to 1)
     * @return
     * @throws UnknownFactTypeException
     */
    public FactType randomKnowledgeGenerator( int forceFirstMinWidth, int depth, int maxDepth, int maxWidth, int maxPhenoms, int maxUnits) throws UnknownFactTypeException {

        // TODO: data validation
        int minWidth = 1;
        FactType factType;
        int type;
        if(depth ==1 && forceFirstMinWidth > 0){
            type = 0;
            minWidth=forceFirstMinWidth;
        }
        else if(depth == maxDepth) type = randInt(1,3);
        else type = randInt(0,3);

        switch(type)
        {
            case 0:
                factType = new CompositeType( "COMPOSITE_" + timeMillis() + "_" + randomString(7) );
                int nChilds = randInt(minWidth, maxWidth);
                for(int i=1; i<nChilds; i++)
                    ((CompositeType)factType).addChild( randomKnowledgeGenerator(0, depth+1, maxDepth, maxWidth, maxPhenoms, maxUnits));

                break;

            case 1:
                factType = new QuantitativeType( "QUANTITATIVE_" + timeMillis() + "_" + randomString(7) );
                int nUnits = randInt(1, maxUnits);
                for(int i=0; i<nUnits; i++)
                {
                    String symbol = randomString(7);
                    ((QuantitativeType)factType).addLegalUnit(  new Unit("UNIT_"+ timeMillis() + "_" + symbol, symbol));
                }
                break;

            case 2:
                factType = new QualitativeType( "QUALITATIVE_" + timeMillis() + "_" + randomString(7) );
                int nPhenom = randInt(1, maxPhenoms);
                for(int i=0; i<nPhenom; i++)
                    ((QualitativeType)factType).addLegalPhenomenon( new Phenomenon("PHENOMENON_" + timeMillis() + "_" + randomString(7) ));
                break;

            case 3:
                factType = new TextualType( "TEXTUAL_" + timeMillis() + "_" + randomString(7) );
                break;

            default: factType=null; throw new UnknownFactTypeException();
        }

        return factType;

    }




    public FactType knowledgeGenerator(String discriminator) {

        CompositeType   rootFactTypes;

        // * * * * * * * * * * * * * KNOWLEDGE MODEL * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        rootFactTypes = new CompositeType("ROOT " + discriminator);

        CompositeType typeAnSangue = new CompositeType("COMPOSITE A " + discriminator);

            QuantitativeType typeGlicemia = new QuantitativeType("QUANTITATIVE  " + discriminator);
            Unit uMgDl = new Unit("Milligrammi per decilitro " + discriminator, "mg/dl " + discriminator);
            typeGlicemia.addLegalUnit(uMgDl);
            typeAnSangue.addChild(typeGlicemia);

            QualitativeType typeBloodType =  new QualitativeType("SUB SUB QUALIT_TYPE 1");
            Phenomenon phBloodA  = new Phenomenon("A " + discriminator);         typeBloodType.addLegalPhenomenon(phBloodA);
            Phenomenon phBloodB  = new Phenomenon("B " + discriminator);         typeBloodType.addLegalPhenomenon(phBloodB);
            Phenomenon phBloodAB = new Phenomenon("AB " + discriminator);        typeBloodType.addLegalPhenomenon(phBloodAB);
            Phenomenon phBlood0  = new Phenomenon("0 " + discriminator );         typeBloodType.addLegalPhenomenon(phBlood0);
        typeAnSangue.addChild(typeBloodType);

        rootFactTypes.addChild(typeAnSangue);




        CompositeType typeAnCardio = new CompositeType("Analisi cardiaca "+ discriminator );

        QuantitativeType typeBpm = new QuantitativeType("battiti per minuto "+ discriminator);
        Unit uBpm = new Unit("battiti per minuto", "bpm "+ discriminator);  typeBpm.addLegalUnit(uBpm);
        typeAnCardio.addChild(typeBpm);

        QuantitativeType typeBloodPressure = new QuantitativeType("Pressione Sanguigna "+ discriminator);
        Unit uPsi = new Unit("pressione psi "+ discriminator, "psi "+ discriminator);       typeBloodPressure.addLegalUnit(uPsi);
        Unit uBar = new Unit("pressione bar "+ discriminator, "bar "+ discriminator);       typeBloodPressure.addLegalUnit(uBar);
        typeAnCardio.addChild(typeBloodPressure);

        rootFactTypes.addChild(typeAnCardio);




        CompositeType typeAnRespiratoria = new CompositeType("Analisi respiratoria (Spiometrica) " + discriminator);

        TextualType typeNotes = new TextualType("Notes " + discriminator);
        typeAnRespiratoria.addChild(typeNotes);

        QuantitativeType typeBlowPressure = new QuantitativeType("Pressione respiratoria " + discriminator);
        typeBlowPressure.addLegalUnit(uPsi);
        typeBlowPressure.addLegalUnit(uBar);
        typeAnRespiratoria.addChild(typeBlowPressure);

        rootFactTypes.addChild(typeAnRespiratoria);

        // * * * * * * * * * * * END KNOWLEDGE MODEL * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

        return rootFactTypes;
    }

}
