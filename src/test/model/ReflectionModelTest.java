package test.model;

import reflectionPattern.modelALS.knowledge.*;
import reflectionPattern.modelALS.knowledge.quantity.ImpossibleConversionException;
import reflectionPattern.modelALS.knowledge.quantity.Quantity;
import reflectionPattern.modelALS.knowledge.quantity.Unit;
import reflectionPattern.modelALS.operational.CompositeFact;
import reflectionPattern.modelALS.operational.QualitativeFact;
import reflectionPattern.modelALS.operational.QuantitativeFact;
import reflectionPattern.modelALS.operational.TextualFact;
import reflectionPattern.utility.composite.out.CompositeTree;
import reflectionPattern.utility.compositeWithAncestors.out.CompositeTreeALS;

import static org.junit.Assert.*;

/**
 * Created by nagash on 02/09/16.
 */
public class ReflectionModelTest {


    @org.junit.Test
    public void testTypeEquals()
    {
        QuantitativeType quantType = new QuantitativeType("QuantType");
        QualitativeType qualType = new QualitativeType("QualType");
        TextualType     textType = new TextualType("TextualType");
        CompositeType   compTypeB = new CompositeType("CompTypeB");
        CompositeType   compTypeA = new CompositeType("CompTypeA");
        compTypeB.addChild(qualType);
        compTypeB.addChild(textType);
        compTypeA.addChild(compTypeB);
        compTypeA.addChild(quantType);



        // Same values and relationships
        QuantitativeType quantType2 = new QuantitativeType("QuantType");
        QualitativeType qualType2 = new QualitativeType("QualType");
        TextualType     textType2 = new TextualType("TextualType");
        CompositeType   compType2B = new CompositeType("CompTypeB");
        CompositeType   compType2A = new CompositeType("CompTypeA");
        compType2B.addChild(qualType2);
        compType2B.addChild(textType2);
        compType2A.addChild(compType2B);
        compType2A.addChild(quantType2);


        // Same values, and Relationships again (test transitive property).
        QuantitativeType quantType0 = new QuantitativeType("QuantType");
        QualitativeType qualType0 = new QualitativeType("QualType");
        TextualType     textType0 = new TextualType("TextualType");
        CompositeType   compType0B = new CompositeType("CompTypeB");
        CompositeType   compType0A = new CompositeType("CompTypeA");
        compType0B.addChild(qualType0);
        compType0B.addChild(textType0);
        compType0A.addChild(compType0B);
        compType0A.addChild(quantType0);

        // Same values, different relationships:
        //      A contains textualType instead of quantType.
        //      B contain quantType instead of textualType.
        QuantitativeType quantType3 = new QuantitativeType("QuantType");
        QualitativeType qualType3 = new QualitativeType("QualType");
        TextualType     textType3 = new TextualType("TextualType");
        CompositeType   compType3B = new CompositeType("CompTypeB");
        CompositeType   compType3A = new CompositeType("CompTypeA");
        compType3B.addChild(qualType3);
        compType3B.addChild(quantType3);
        compType3A.addChild(compType3B);
        compType3A.addChild(textType3);


        // Same values, different relationships:
        //      A and B relationships inverted
        QuantitativeType quantType4 = new QuantitativeType("QuantType");
        QualitativeType qualType4 = new QualitativeType("QualType");
        TextualType     textType4 = new TextualType("TextualType");
        CompositeType   compType4B = new CompositeType("CompTypeB");
        CompositeType   compType4A = new CompositeType("CompTypeA");
        compType4A.addChild(qualType4);
        compType4A.addChild(quantType4);
        compType4B.addChild(compType4A);
        compType4B.addChild(textType4);




        // Null test
        assertFalse( compTypeA.equals(null) );
        assertFalse( compTypeB.equals(null) );
        assertFalse( textType.equals(null) );
        assertFalse( qualType.equals(null) );
        assertFalse( quantType.equals(null) );



        // Equal to self (reflective property)
        assertTrue(compTypeA .equals(compTypeA ) );
        assertTrue(compTypeB .equals(compTypeB ) );
        assertTrue(textType  .equals(textType  ) );
        assertTrue(qualType  .equals(qualType  ) );
        assertTrue(quantType .equals(quantType  ) );

        // Equal to other istance with same values and same relationship (symmetric property):
        if(compType2A .equals(compTypeA ))
            assertTrue(compTypeA .equals(compType2A ) );

        assertTrue(compType2A .equals(compTypeA ) );
        assertTrue(compType2B .equals(compTypeB ) );
        assertTrue(textType2  .equals(textType  ) );
        assertTrue(qualType2  .equals(qualType  ) );
        assertTrue(quantType2 .equals(quantType  ) );

        assertTrue(compTypeA .equals(compType2A ) );
        assertTrue(compTypeB .equals(compType2B ) );
        assertTrue(textType  .equals(textType2  ) );
        assertTrue(qualType  .equals(qualType2  ) );
        assertTrue(quantType .equals(quantType2  ) );





        // Transitive property:
        if(compTypeA .equals(compType2A )  &&  (compTypeA .equals(compType0A )))
            assertTrue(compType0A .equals(compType2A ) );




        // Equal to other istance with same values but with other relationships
        assertFalse(compType3A .equals(compTypeA) );
        assertFalse(compType3B .equals(compTypeB) );
        // (No-Composite-Type are equals, but CompositeType are not equals!)
        assertTrue(textType3  .equals(textType) );
        assertTrue(qualType3  .equals(qualType) );
        assertTrue(quantType3 .equals(quantType) );

        assertFalse(compType4A .equals(compTypeA) );
        assertFalse(compType4B .equals(compTypeB) );
        // (No-Composite-Type are equals, but CompositeType are not equals!)
        assertTrue(textType4  .equals(textType) );
        assertTrue(qualType4  .equals(qualType) );
        assertTrue(quantType4 .equals(quantType) );
    }





    @org.junit.Test
    public void testFactEquals() throws QuantitativeFact.IllegalQuantitativeUnitException,
                                        QualitativeFact.IllegalQualitativePhenomenonException,
                                        CompositeFact.IllegalFactTypeException
    {
        QuantitativeType quantType = new QuantitativeType("QuantType");
        QualitativeType qualType = new QualitativeType("QualType");
        TextualType     textType = new TextualType("TextualType");
        CompositeType   compTypeB = new CompositeType("CompTypeB");
        CompositeType   compTypeA = new CompositeType("CompTypeA");
        compTypeB.addChild(qualType);
        compTypeB.addChild(textType);
        compTypeA.addChild(compTypeB);
        compTypeA.addChild(quantType);

        // Same values and relationships
        QuantitativeType quantType2 = new QuantitativeType("QuantType");
        QualitativeType qualType2 = new QualitativeType("QualType");
        TextualType     textType2 = new TextualType("TextualType");
        CompositeType   compType2B = new CompositeType("CompTypeB");
        CompositeType   compType2A = new CompositeType("CompTypeA");
        compType2B.addChild(qualType2);
        compType2B.addChild(textType2);
        compType2A.addChild(compType2B);
        compType2A.addChild(quantType2);



        // Same values and relationships, different legal phenomenon (see down)
        QuantitativeType quantType2lf = new QuantitativeType("QuantType");
        QualitativeType qualType2lf = new QualitativeType("QualType");
        TextualType     textType2lf = new TextualType("TextualType");
        CompositeType   compType2lfB = new CompositeType("CompTypeB");
        CompositeType   compType2lfA = new CompositeType("CompTypeA");
        compType2lfB.addChild(qualType2lf);
        compType2lfB.addChild(textType2lf);
        compType2lfA.addChild(compType2lfB);
        compType2lfA.addChild(quantType2lf);


        // Same values, different relationships:
        //      A contains textualType instead of quantType.
        //      B contain quantType instead of textualType.
        QuantitativeType quantType3 = new QuantitativeType("QuantType");
        QualitativeType qualType3 = new QualitativeType("QualType");
        TextualType     textType3 = new TextualType("TextualType");
        CompositeType   compType3B = new CompositeType("CompTypeB");
        CompositeType   compType3A = new CompositeType("CompTypeA");
        compType3B.addChild(qualType3);
        compType3B.addChild(quantType3);
        compType3A.addChild(compType3B);
        compType3A.addChild(textType3);



        Unit legalUnit = new Unit("Kilogram", "Kg");
        quantType.addLegalUnit(legalUnit);
        quantType2.addLegalUnit(legalUnit);
        quantType2lf.addLegalUnit(legalUnit);
        quantType3.addLegalUnit(legalUnit);



        Phenomenon legalPhenomenon = new Phenomenon("phenomenon!!");
        Phenomenon differentPhenomenon = new Phenomenon("phenomenon different!");
        Phenomenon notSoLegalPhenomenon = new Phenomenon("Not-so-legal phenomenon..");

        qualType.addLegalPhenomenon(legalPhenomenon);
        qualType.addLegalPhenomenon(differentPhenomenon);

        qualType2.addLegalPhenomenon(legalPhenomenon);
        qualType2.addLegalPhenomenon(differentPhenomenon);

        qualType2lf.addLegalPhenomenon(legalPhenomenon);
        qualType2lf.addLegalPhenomenon(differentPhenomenon);

        qualType3.addLegalPhenomenon(legalPhenomenon);
        qualType3.addLegalPhenomenon(differentPhenomenon);


        assertTrue( compTypeB.equals(compType2B));
       // assertEquals( compTypeB, compType2B);

        qualType2lf.addLegalPhenomenon(notSoLegalPhenomenon);



        QuantitativeFact quantitativeFact = new QuantitativeFact(quantType, 30, legalUnit);
        QualitativeFact  qualitativeFact  = new QualitativeFact(qualType, legalPhenomenon);
        TextualFact      textualFact      = new TextualFact(textType, "textualType");
        CompositeFact    compositeFactA   = new CompositeFact(compTypeA);
        CompositeFact    compositeFactB   = new CompositeFact(compTypeB);
        compositeFactB.addChild(qualitativeFact);
        compositeFactB.addChild(textualFact);
        compositeFactA.addChild(compositeFactB);
        compositeFactA.addChild(quantitativeFact);



        QuantitativeFact quantitativeFact_d = new QuantitativeFact(quantType, 50, legalUnit);
        QualitativeFact  qualitativeFact_d  = new QualitativeFact(qualType, differentPhenomenon);
        TextualFact      textualFact_d      = new TextualFact(textType, "textualType different!");
        CompositeFact    compositeFact_dA   = new CompositeFact(compTypeA);
        CompositeFact    compositeFact_dB   = new CompositeFact(compTypeB);
        compositeFact_dB.addChild(qualitativeFact_d);
        compositeFact_dB.addChild(textualFact_d);
        compositeFact_dA.addChild(compositeFact_dB);
        compositeFact_dA.addChild(quantitativeFact_d);




        // Same Type, values and relationships
        QuantitativeFact quantitativeFact_s = new QuantitativeFact(quantType, 30, legalUnit);
        QualitativeFact  qualitativeFact_s  = new QualitativeFact(qualType, legalPhenomenon);
        TextualFact      textualFact_s      = new TextualFact(textType, "textualType");
        CompositeFact    compositeFact_sA   = new CompositeFact(compTypeA);
        CompositeFact    compositeFact_sB   = new CompositeFact(compTypeB);
        compositeFact_sB.addChild(qualitativeFact_s);
        compositeFact_sB.addChild(textualFact_s);
        compositeFact_sA.addChild(compositeFact_sB);
        compositeFact_sA.addChild(quantitativeFact_s);


        // Different (but equal) Type, same values and relationships
        QuantitativeFact quantitativeFact2 = new QuantitativeFact(quantType2, 30, legalUnit);
        QualitativeFact  qualitativeFact2  = new QualitativeFact(qualType2, legalPhenomenon);
        TextualFact      textualFact2      = new TextualFact(textType2, "textualType");
        CompositeFact    compositeFact2A   = new CompositeFact(compType2A);
        CompositeFact    compositeFact2B   = new CompositeFact(compType2B);
        compositeFact2B.addChild(qualitativeFact2);
        compositeFact2B.addChild(textualFact2);
        compositeFact2A.addChild(compositeFact2B);
        compositeFact2A.addChild(quantitativeFact2);


        // Different (but equal) Type, same values and relationships
        QuantitativeFact quantitativeFact2lf = new QuantitativeFact(quantType2lf, 30, legalUnit);
        QualitativeFact  qualitativeFact2lf  = new QualitativeFact(qualType2lf, legalPhenomenon);
        TextualFact      textualFact2lf      = new TextualFact(textType2lf, "textualType");
        CompositeFact    compositeFact2lfA   = new CompositeFact(compType2lfA);
        CompositeFact    compositeFact2lfB   = new CompositeFact(compType2lfB);
        compositeFact2lfB.addChild(qualitativeFact2lf);
        compositeFact2lfB.addChild(textualFact2lf);
        compositeFact2lfA.addChild(compositeFact2lfB);
        compositeFact2lfA.addChild(quantitativeFact2lf);

        // Different Type with different relationships, same values
        //      A contains textualType instead of quantType.
        //      B contain quantType instead of textualType.
        QuantitativeFact quantitativeFact3 = new QuantitativeFact(quantType3, 30, legalUnit);
        QualitativeFact  qualitativeFact3  = new QualitativeFact(qualType3, legalPhenomenon);
        TextualFact      textualFact3      = new TextualFact(textType3, "textualType");
        CompositeFact    compositeFact3A   = new CompositeFact(compType3A);
        CompositeFact    compositeFact3B   = new CompositeFact(compType3B);
        compositeFact3B.addChild(qualitativeFact3);
        compositeFact3B.addChild(quantitativeFact3); // swapped
        compositeFact3A.addChild(compositeFact3B);
        compositeFact3A.addChild(textualFact3);     // swapped


        // Equals to self instance with same relationship and same values:
        assertTrue( quantitativeFact .equals( quantitativeFact ));
        assertTrue( qualitativeFact  .equals( qualitativeFact  ));
        assertTrue( textualFact      .equals( textualFact      ));
        assertTrue( compositeFactB   .equals( compositeFactB   ));
        assertTrue( compositeFactA   .equals( compositeFactA   ));

        // Equals to other instance with same relationship and same values:
        assertTrue( quantitativeFact .equals( quantitativeFact_s));
        assertTrue( qualitativeFact  .equals( qualitativeFact_s ));
        assertTrue( textualFact      .equals( textualFact_s     ));
        assertTrue( compositeFactB   .equals( compositeFact_sB  ));
        assertTrue( compositeFactA   .equals( compositeFact_sA  ));

        assertTrue( quantitativeFact_s .equals( quantitativeFact));
        assertTrue( qualitativeFact_s  .equals( qualitativeFact ));
        assertTrue( textualFact_s      .equals( textualFact     ));
        assertTrue( compositeFact_sB   .equals( compositeFactB  ));
        assertTrue( compositeFact_sA   .equals( compositeFactA  ));


        // Equals to self instance with same relationship but without same values:
        assertFalse( quantitativeFact .equals( quantitativeFact_d ));
        assertFalse( qualitativeFact  .equals( qualitativeFact_d  ));
        assertFalse( textualFact      .equals( textualFact_d      ));
        assertFalse( compositeFactB   .equals( compositeFact_dB   ));
        assertFalse( compositeFactA   .equals( compositeFact_dA   ));



        // Equals to other instance with different type (but with same values and relationships.. not really same relationships!),
        // and same values.. but with a legal phenomenon more in the QualitativeFact! (So with a different relationship in the Type, QualitativeType).
        assertTrue( quantitativeFact .equals( quantitativeFact2lf ));
            // qualitativeFact2 it's of tipe QualitativeType2f, that differs from the original:
            // it has a legal phenomenon more than the original QualitativeType,so this check must fail (not equal)
        assertFalse( qualitativeFact  .equals( qualitativeFact2lf  ));
        assertTrue( textualFact      .equals( textualFact2lf       ));
        assertFalse( compositeFactB   .equals( compositeFact2lfB   ));//B contains qualitative fact.. so this check must fail (not equal)
        assertFalse( compositeFactA   .equals( compositeFact2lfA   ));//A contains B that contains qualitative fact.. so this check must fail (not equal)







        // Equals to other instance with different type (but with same values and relationships), and same values:
        assertTrue( quantitativeFact .equals( quantitativeFact2 ));
        assertTrue( qualitativeFact  .equals( qualitativeFact2  ));
        assertTrue( textualFact      .equals( textualFact2      ));
        assertTrue( compositeFactB   .equals( compositeFact2B   ));
        assertTrue( compositeFactA   .equals( compositeFact2A   ));


        // Equals to other instance with different type (with same values but different relationships), and same values:
        assertTrue( quantitativeFact .equals( quantitativeFact3 ));
        assertTrue( qualitativeFact  .equals( qualitativeFact3  ));
        assertTrue( textualFact      .equals( textualFact3      ));
        assertFalse( compositeFactB   .equals( compositeFact3B   ));
        assertFalse( compositeFactA   .equals( compositeFact3A   ));





        /* NB
            Ho capito un problema che affliggeva questo test: quando aggiungo un oggetto in una HashSet, esso verrá indicizzato
            all'interno di esso tramite il suo hash generato.

            Se vado a modificare l'oggetto dopo averlo inserito, e tali modifiche vanno a modificare l'oggetto, va a finire
            che vado a modificare l'hash generato dall'oggetto! Ma questa modifica on viene riflessa sull'hashSet in cui tale
            oggetto era memorizzato, e quindi se vado a ricercare tale oggetto all'interno dell'hashset il risultato sará che non
            lo troverò (è cambiato lo hash dell'oggetto!).

            Questo problema affliggeva il test:
                    assertTrue( compositeFactB   .equals( compositeFact2B   ));
            e simili.

            Questo perché il qualTypeB ed il qualType2B hanno inizialmente 0 Fenomeni.
            Nel momento in cui hanno 0 fenomeni, li aggiungo rispettivamente alle composizioni compTypeB e compType2B, che li
            inseriscono all'interno di una HashSet secondo lo hash generato al momento iniziale (in cui ancora non avevo inserito i fenomeni).

            Dopo di che vado ad inserire i fenomeni ai due qualTypeB, e di conseguenza cambia lo hash generato.
            Di conseguenza quando vado a cercare all'interno di compTypeB (o compType2B) la presenza del tipo qualType2B (o qualTypeB)
            tramite _childTypes.contains(qualType2B), questo mi tornerá false, poiché lo hash generato da qualType2B non è lo stesso
            hash generato da qualTypeB al momento in cui è stato inserito in compTypeB.

            Per risolvere il problema ho commentato in QualitativeType le righe in cui, generando lo HashSet, prendo in considerazione
            gli hash generati dai Phenomenon legali.

            La stessa cosa vale per il QuantitativeType e le Unit legali.


            LO STESSO PROBLEMA POTREBBE AFFLIGGERE CompositeType E CompositeFact POICHÉ NEL GENERARE LO HASH PRENDO IN CONSIDERAZIONE
            ANCHE TUTTI I FIGLI.

            SE AGGIUNGO UN COMPOSITE-TYPE-B AD UN ALTRO COMPOSITE-TYPE-A PADRE, E SUCCESSIVAMENTE AGGIUNGO UN TYPE-C AL
             COMPOSITE-TYPE-B, OTTENGO LO STESSO IDENTICO PROBLEMA DESCRITTO SOPRA.
            DOBBIAMO QUINDI GENERARE  LE GERARCHIE CON L'ORDINE CORRETTO!!
            (Oppure semplicemente rimuovere le composizioni/set dalla generazione dello hash).

             (stessa identica cosa per CompositeFact, inoltre CompositeFact ha lo stesso problema sia per i CompositeFact figli che
             per i Type figli del CompositeType a cui è associato)


            TODO: per sicurezza, rimuovere le composizioni/set dalla generazione dello hash, in CompositeType ed in CompositeFact.
         */



    }


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



        CompositeTreeALS.printTree(analisiMedicoSportiva);




        assertTrue(true);

    }


}