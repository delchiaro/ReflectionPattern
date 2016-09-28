package reflectionPattern.dataGeneration;

import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.modelExtension.MyAggregatePhenomenon;
import reflectionPattern.modelExtension.MySubPhenomeon;

import static reflectionPattern.dataGeneration.RandomUtils.randInt;
import static reflectionPattern.dataGeneration.RandomUtils.randomString;
import static reflectionPattern.dataGeneration.RandomUtils.timeMillis;


/**
 * Created by nagash on 12/09/16.
 */
public class FactTypeGenerator {

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
                factType = new CompositeType( "COMPOSITE_" + timeMillis() + "_" + randomString(7) );
                int nChilds;
                if(depth==0)
                    nChilds = randInt( param.getRootChildsRange().inf()>0 ? param.getRootChildsRange().inf() : 1, param.getRootChildsRange().sup());
                else nChilds = randInt(param.getCompChildsRange());

                for(int i=0; i<nChilds; i++)
                    ((CompositeType)factType).addChild( randomFactType(depth+1));
                break;

            case 1:
                factType = new QuantitativeType( "QUANTITATIVE_" + timeMillis() + "_" + randomString(7) );
                int nUnits = randInt(param.getUnitsRange());
                for(int i=0; i<nUnits; i++)
                {
                    String symbol = randomString(7);
                    ((QuantitativeType)factType).addLegalUnit(  new Unit("UNIT_"+ timeMillis() + "_" + symbol, symbol));
                }
                break;

            case 2:
                factType = new QualitativeType( "QUALITATIVE_" + timeMillis() + "_" + randomString(7) );
                int nPhenom = randInt(param.getUnitsRange());
                for(int i=0; i<nPhenom; i++)
                {
                    Phenomenon p;
                    String value = "PHENOMENON_" + timeMillis() + "_" + randomString(7);
                    String code = "CODE_" + timeMillis() + "_" + randomString(3);

                    if(param.getSubPhenomenon() == MySubPhenomeon.class)
                        p = new MySubPhenomeon( code, value);
                    else if(param.getSubPhenomenon() == MyAggregatePhenomenon.class)
                    {
                        p = new Phenomenon(value);
                        new MyAggregatePhenomenon(p,code, value);
                    }
                    else
                        p = new Phenomenon(value);

                    ((QualitativeType) factType).addLegalPhenomenon(p);
                }
                break;

            case 3:
                factType = new TextualType( "TEXTUAL_" + timeMillis() + "_" + randomString(7) );
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
