package test.model;

import reflectionPattern.IO.compositeAdapter.FactTypeCompositeAdapter;
import reflectionPattern.IO.OutputManager;
import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.ImpossibleConversionException;
import reflectionPattern.model.knowledge.quantity.Quantity;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.operational.QualitativeFact;
import reflectionPattern.model.operational.QuantitativeFact;
import reflectionPattern.model.operational.TextualFact;

import static org.junit.Assert.*;

/**
 * Created by nagash on 02/09/16.
 */
public class ReflectionModelTest {

    @org.junit.Test
    public void testQuantityConversion() throws Exception {
        Unit kg = new Unit("kilogrammo", "kg");
        Unit g = new Unit("grammo", "g");
        Unit cl = new Unit("centilitro", "cl");

        kg.addConversionRatio(g, 1000);

        Quantity peso = new Quantity(80, kg);
        Quantity pesog = peso.convert(g);

        assertEquals(80000.0, pesog.getValue());

        try{
            Quantity peso_cl = peso.convert(cl);
        }
        catch (ImpossibleConversionException e)
        {
            assertTrue(true);
        }

        Quantity peso_kg_again = pesog.convert(kg);
        assertEquals(80.0, peso_kg_again.getValue());
    }




    @org.junit.Test
    public void testModelBasic2(){
        Unit bpm = new Unit("battiti per minuto", "bpm");
        Unit psi = new Unit("pressione psi", "psi");
        Unit bar = new Unit("pressione bar", "bar");

        QuantitativeType bpmType = new QuantitativeType("battiti per minuto");
        bpmType.addLegalUnit(bpm);

        QuantitativeType bloodPresureType = new QuantitativeType("Pressione Sanguigna");
        bloodPresureType.addLegalUnit(psi);
        bloodPresureType.addLegalUnit(bar);


        QualitativeType bloodGroupType =     new QualitativeType("Blood Group");
        TextualType notesType =             new TextualType("Notes");

        QuantitativeFact myBpm;
        QuantitativeFact myBloodPressure;
        try {
            myBpm = new QuantitativeFact(bpmType, new Quantity(80, bpm));
            myBloodPressure = new QuantitativeFact(bloodPresureType, new Quantity(120, psi));
        } catch (QuantitativeFact.IllegalQuantitativeUnitException e) {
            e.printStackTrace();
            assertFalse(true);// non dev arrivare qua poiché le unit usate sono tutti consentite.
        }

        QuantitativeFact myBpm2;
        try{
            myBpm2 = new QuantitativeFact(bpmType, new Quantity(80, psi));
        } catch (QuantitativeFact.IllegalQuantitativeUnitException e) {
            //e.printStackTrace();
            assertTrue(true); // devo arrivare qua poiché il psi non è una Unit consentita da bpmType.
        }


        QualitativeType bloodType =     new QualitativeType("Blood Type");
        Phenomenon phBloodA     = new Phenomenon("A");  bloodType.addLegalPhenomenon(phBloodA);
        Phenomenon phBloodB     = new Phenomenon("B");  bloodType.addLegalPhenomenon(phBloodB);
        Phenomenon phBloodAB    = new Phenomenon("AB"); bloodType.addLegalPhenomenon(phBloodAB);
        Phenomenon phBlood0     = new Phenomenon("0");  bloodType.addLegalPhenomenon(phBlood0);
        Phenomenon phBloodXX    = new Phenomenon("XX");


        QualitativeFact myBloodType;
        QualitativeFact yourBloodType;
        QualitativeFact hisBloodType;
        try {
            myBloodType =   new QualitativeFact(bloodType, phBloodAB );
            yourBloodType = new QualitativeFact(bloodType, phBloodA );
            hisBloodType =  new QualitativeFact(bloodType, phBlood0 );
        } catch (QualitativeFact.IllegalQualitativePhenomenonException e) {
            e.printStackTrace();
            assertFalse(true);// non dev arrivare qua poiché i phenomenon usati sono tutti consentiti dal Type bloodType.
        }

        QualitativeFact anotherBlood;
        try {
            anotherBlood = new QualitativeFact(bloodType, phBloodXX );
        } catch (QualitativeFact.IllegalQualitativePhenomenonException e) {
            //e.printStackTrace();
            assertTrue(true); // devo arrivare qua poiché il phBloodXX non è un phenomenon consentito dal bloodType.
            anotherBlood = null;
        }


        TextualFact myNote = new TextualFact(notesType, "Note sul paziente xyz, la pressione è alterata da una massiccia dose di xxxx.");

        CompositeType analisiMedicoSportiva = new CompositeType("Analisi medico sportive");
        CompositeType analisiDelSangue = new CompositeType("Analisi del sangue");
        CompositeType analisiDelFiato = new CompositeType("Analisi del fiato");

        analisiMedicoSportiva.addChild(analisiDelSangue);
        analisiMedicoSportiva.addChild(analisiDelFiato);

        analisiDelSangue.addChild(bloodGroupType);
        analisiDelSangue.addChild(bloodPresureType);

        analisiDelSangue.addChild(bloodGroupType);
        analisiDelSangue.addChild(bloodPresureType);
        analisiMedicoSportiva.addChild(notesType);


        String str = OutputManager.adapterExplorer(new FactTypeCompositeAdapter(analisiMedicoSportiva));
        System.out.print(str);




        assertTrue(true);

    }


}