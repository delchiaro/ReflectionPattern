package reflectionPattern.test.modelExtension;

import org.junit.Ignore;
import org.junit.Test;
import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.knowledge.MyAggregatePhenomenon;
import reflectionPattern.model.knowledge.MySubPhenomeon;
import reflectionPattern.persistency.PersistencyHelper;

import javax.persistence.EntityTransaction;

/**
 * Created by nagash on 22/09/16.
 */
public class testPhenomenonExt {

    CompositeType rootFactTypes;

    @Ignore
    @Test
    public void testInheritance() {


        // * * * * * * * * * * * * * KNOWLEDGE MODEL * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        rootFactTypes = new CompositeType("Analisi Medico Sportiva");


        CompositeType typeAnSangue = new CompositeType("Analisi del sangue");

        QuantitativeType typeGlicemia = new QuantitativeType("Livelli di glicemia del sangue ");
        Unit uMgDl = new Unit("Milligrammi per decilitro", "mg/dl");
        typeGlicemia.addLegalUnit(uMgDl);
        typeAnSangue.addChild(typeGlicemia);

        QualitativeType typeBloodType =  new QualitativeType("Gruppo sanguigno");
        Phenomenon phBloodA  = new MySubPhenomeon("1234", "A");        typeBloodType.addLegalPhenomenon(phBloodA);
        Phenomenon phBloodB  = new MySubPhenomeon("1245","B");         typeBloodType.addLegalPhenomenon(phBloodB);
        Phenomenon phBloodAB = new MySubPhenomeon("4254","AB");        typeBloodType.addLegalPhenomenon(phBloodAB);
        Phenomenon phBlood0  = new MySubPhenomeon("2543","0");         typeBloodType.addLegalPhenomenon(phBlood0);
        typeAnSangue.addChild(typeBloodType);

        rootFactTypes.addChild(typeAnSangue);




        CompositeType typeAnCardio = new CompositeType("Analisi cardiaca");

        QuantitativeType typeBpm = new QuantitativeType("battiti per minuto");
        Unit uBpm = new Unit("battiti per minuto", "bpm");  typeBpm.addLegalUnit(uBpm);
        typeAnCardio.addChild(typeBpm);

        QuantitativeType typeBloodPressure = new QuantitativeType("Pressione Sanguigna");
        Unit uPsi = new Unit("pressione psi", "psi");       typeBloodPressure.addLegalUnit(uPsi);
        Unit uBar = new Unit("pressione bar", "bar");       typeBloodPressure.addLegalUnit(uBar);
        typeAnCardio.addChild(typeBloodPressure);

        rootFactTypes.addChild(typeAnCardio);




        CompositeType typeAnRespiratoria = new CompositeType("Analisi respiratoria (Spiometrica) ");

        TextualType typeNotes = new TextualType("Notes");
        typeAnRespiratoria.addChild(typeNotes);

        QuantitativeType typeBlowPressure = new QuantitativeType("Pressione respiratoria (???)");
        typeBlowPressure.addLegalUnit(uPsi);
        typeBlowPressure.addLegalUnit(uBar);
        typeAnRespiratoria.addChild(typeBlowPressure);

        rootFactTypes.addChild(typeAnRespiratoria);

        // * * * * * * * * * * * END KNOWLEDGE MODEL * * * * * * * * * * * * * * * * * * * * * * * * * * * * *








        // HIBERNATE SAVING ENTITIES:
        PersistencyHelper ph = new PersistencyHelper(true).connect();


        EntityTransaction saveTransact = ph.newTransaction();

        saveTransact.begin();
        ph.persist(rootFactTypes);
        saveTransact.commit();



    }


    @Test
    public void testComposition() {


        // * * * * * * * * * * * * * KNOWLEDGE MODEL * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        rootFactTypes = new CompositeType("Analisi Medico Sportiva");


        CompositeType typeAnSangue = new CompositeType("Analisi del sangue");

        QuantitativeType typeGlicemia = new QuantitativeType("Livelli di glicemia del sangue ");
        Unit uMgDl = new Unit("Milligrammi per decilitro", "mg/dl");
        typeGlicemia.addLegalUnit(uMgDl);
        typeAnSangue.addChild(typeGlicemia);

        QualitativeType typeBloodType =  new QualitativeType("Gruppo sanguigno");
        Phenomenon phBloodA  = new Phenomenon("A");         typeBloodType.addLegalPhenomenon(phBloodA);
        Phenomenon phBloodB  = new Phenomenon("B");         typeBloodType.addLegalPhenomenon(phBloodB);
        Phenomenon phBloodAB = new Phenomenon("AB");        typeBloodType.addLegalPhenomenon(phBloodAB);
        Phenomenon phBlood0  = new Phenomenon("0");         typeBloodType.addLegalPhenomenon(phBlood0);

        MyAggregatePhenomenon subA = new MyAggregatePhenomenon(phBloodA, "1234", "A");
        MyAggregatePhenomenon subB = new MyAggregatePhenomenon(phBloodA, "1245", "B");
        MyAggregatePhenomenon subAB = new MyAggregatePhenomenon(phBloodA, "4254", "AB");
        MyAggregatePhenomenon sub0 = new MyAggregatePhenomenon(phBloodA, "2543", "B");


        typeAnSangue.addChild(typeBloodType);

        rootFactTypes.addChild(typeAnSangue);




        CompositeType typeAnCardio = new CompositeType("Analisi cardiaca");

        QuantitativeType typeBpm = new QuantitativeType("battiti per minuto");
        Unit uBpm = new Unit("battiti per minuto", "bpm");  typeBpm.addLegalUnit(uBpm);
        typeAnCardio.addChild(typeBpm);

        QuantitativeType typeBloodPressure = new QuantitativeType("Pressione Sanguigna");
        Unit uPsi = new Unit("pressione psi", "psi");       typeBloodPressure.addLegalUnit(uPsi);
        Unit uBar = new Unit("pressione bar", "bar");       typeBloodPressure.addLegalUnit(uBar);
        typeAnCardio.addChild(typeBloodPressure);

        rootFactTypes.addChild(typeAnCardio);




        CompositeType typeAnRespiratoria = new CompositeType("Analisi respiratoria (Spiometrica) ");

        TextualType typeNotes = new TextualType("Notes");
        typeAnRespiratoria.addChild(typeNotes);

        QuantitativeType typeBlowPressure = new QuantitativeType("Pressione respiratoria (???)");
        typeBlowPressure.addLegalUnit(uPsi);
        typeBlowPressure.addLegalUnit(uBar);
        typeAnRespiratoria.addChild(typeBlowPressure);

        rootFactTypes.addChild(typeAnRespiratoria);

        // * * * * * * * * * * * END KNOWLEDGE MODEL * * * * * * * * * * * * * * * * * * * * * * * * * * * * *



        // HIBERNATE SAVING ENTITIES:
        PersistencyHelper ph = new PersistencyHelper(true).connect();


        EntityTransaction saveTransact = ph.newTransaction();

        saveTransact.begin();
        ph.persist(rootFactTypes);
        ph.persist(subA);
        ph.persist(subB);
        ph.persist(subAB);
        ph.persist(sub0);

        saveTransact.commit();


    }


}
