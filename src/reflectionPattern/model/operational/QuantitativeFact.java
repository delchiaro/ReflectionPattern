/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import reflectionPattern.model.knowledge.QuantitativeType;
import reflectionPattern.model.knowledge.quantity.Quantity;
import reflectionPattern.model.knowledge.quantity.Unit;

import javax.persistence.*;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("QUANTITATIVE")
public class QuantitativeFact extends Fact {

    // ONLY FOR HIBERNATE:
    @Embedded
    private Quantity quantity;

    protected QuantitativeFact(){}

    public QuantitativeFact(QuantitativeType factType, Number value, Unit measurementUnit) throws IllegalQuantitativeUnitException {
        this(factType, new Quantity(value, measurementUnit));

    }
    public QuantitativeFact(QuantitativeType factType, Quantity quantity) throws IllegalQuantitativeUnitException {
        super(factType);
        if(factType.isUnitLegal(quantity.getUnit()) == false )
            throw new IllegalQuantitativeUnitException();
        else this.quantity = quantity;

    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  QuantitativeFact)) return false;
        QuantitativeFact qFact = (QuantitativeFact) obj;

        // TODO: implementare una logica diversa senza richiamare super.equals(), in modo da considerare uguali
        // due numeri con valori diversi ma che convertiti alla stessa unitá di misura risultano di egual valore.
        if(super.equals(qFact) && quantity.equals(qFact.quantity))
            return true;
        else return false;
    }

    public class IllegalQuantitativeUnitException extends IllegalValueException {}



    @Override
    public String toString() {
        return super.toString() + ": " + this.quantity.getValue();
    }
}
