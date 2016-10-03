package reflectionPattern.dataGeneration;

import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.knowledge.MyAggregatePhenomenon;
import reflectionPattern.model.knowledge.MySubPhenomeon;

import static reflectionPattern.dataGeneration.RandomUtils.randInt;
import static reflectionPattern.dataGeneration.RandomUtils.randomString;
import static reflectionPattern.dataGeneration.RandomUtils.uniqueTimeID;


/**
 * Created by nagash on 12/09/16.
 */
public class FactTypeGenerator {
    private final static int RNDM_STR_LEN = 3;

    private FactTypeGeneratorParam param;
    public FactTypeGeneratorParam getParam() { return param; }

    public FactTypeGenerator() {
        this.param = new FactTypeGeneratorParam();
    }
    public FactTypeGenerator(FactTypeGeneratorParam param) {
        this.param = param;
    }

    private boolean metMinimumLevel;
    public FactType randomFactType() {
        if(param.getRootChildsRange().inf()>0 && param.getDepthRange().sup()==0)
            return null; // exception

        metMinimumLevel = false;
        return randomFactType(0);
    }


    private FactType randomFactType(int depth) {

        int minWidth = 1;
        FactType factType;
        int type;

        if(depth == 0 && param.getRootChildsRange().inf() > 0)
            type = 0;
        else if(param.isLeafOnlyAtLowerLevel() )
        {
            if( depth < param.getDepthRange().sup())
                type = 0;
            else type = randInt(1,3);
        }
        else
        {
            if( depth < param.getDepthRange().sup())
                type = randInt(0,3);
            else type = randInt(1,3);

            if ( ! metMinimumLevel ) {
                // se non abbiamo ancora incontrato il livello minimo da raggiungere, forziamo un composite type.
                if(depth == param.getDepthRange().inf())
                    metMinimumLevel = true;
                else type = 0;
            }

        }




        switch(type)
        {
            case 0:
                factType = new CompositeType( "COMPOSITE_" + uniqueTimeID() + "_" + randomString(RNDM_STR_LEN) );
                int nChilds;
                if(depth==0)
                    nChilds = randInt( param.getRootChildsRange().inf()>0 ? param.getRootChildsRange().inf() : 1, param.getRootChildsRange().sup());
                else nChilds = randInt(param.getCompChildsRange());

                for(int i=0; i<nChilds; i++)
                    ((CompositeType)factType).addChild( randomFactType(depth+1));
                break;

            case 1:
                factType = new QuantitativeType( "QUANTITATIVE_" + uniqueTimeID() + "_" + randomString(RNDM_STR_LEN) );
                int nUnits = randInt(param.getUnitsRange());
                for(int i=0; i<nUnits; i++)
                {
                    String symbol = randomString(RNDM_STR_LEN);
                    ((QuantitativeType)factType).addLegalUnit(  new Unit("UNIT_"+ uniqueTimeID() + "_" + symbol, symbol));
                }
                break;

            case 2:
                factType = new QualitativeType( "QUALITATIVE_" + uniqueTimeID() + "_" + randomString(RNDM_STR_LEN) );
                int nPhenom = randInt(param.getUnitsRange());
                for(int i=0; i<nPhenom; i++)
                {
                    Phenomenon p;
                    String value =  "_" + uniqueTimeID() + "_" + randomString(RNDM_STR_LEN);
                    String code = "CODE_" + uniqueTimeID() + "_" + randomString(RNDM_STR_LEN);

                    if(param.getSubPhenomenon() == MySubPhenomeon.class)
                        p = new MySubPhenomeon( code, MySubPhenomeon.class.getSimpleName() + value);
                    else if(param.getSubPhenomenon() == MyAggregatePhenomenon.class)
                    {
                        value =  MyAggregatePhenomenon.class.getSimpleName() + value;
                        p = new Phenomenon(value);
                        new MyAggregatePhenomenon(p,code, value);
                    }
                    else
                        p = new Phenomenon( Phenomenon.class.getSimpleName() + value);

                    ((QualitativeType) factType).addLegalPhenomenon(p);
                }
                break;

            case 3:
                factType = new TextualType( "TEXTUAL_" + uniqueTimeID() + "_" + randomString(RNDM_STR_LEN) );
                break;

            default: factType=null;
        }

        return factType;

    }


    public static FactType fixedFactType(String discriminator) {

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
