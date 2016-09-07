/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.operational;

import reflection.model.knowledge.QuantitativeType;
import reflection.model.knowledge.quantity.Quantity;
import reflection.model.knowledge.quantity.Unit;

import javax.persistence.*;


@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("QUANTITATIVE")
public class QuantitativeFact extends Fact<Number> {

    // ONLY FOR HIBERNATE:
    private Unit unit = null;

    protected QuantitativeFact(){}

    public QuantitativeFact(QuantitativeType factType, Number value, Unit measurementUnit) throws IllegalQuantitativeUnitException {
        super(factType, value);
        if(factType.isUnitLegal(measurementUnit) == false )
            throw new IllegalQuantitativeUnitException();
        else this.unit = measurementUnit;
    }
    public QuantitativeFact(QuantitativeType factType, Quantity quantity) throws IllegalQuantitativeUnitException {
        this(factType, quantity.getValue(), quantity.getUnit());
    }



    @ManyToOne (fetch=FetchType.LAZY)
    public Unit getUnit() { return unit; }
    protected void setUnit(Unit unit) throws IllegalQuantitativeUnitException {
        if( ((QuantitativeType)(super.getType())).isUnitLegal(unit) == false )
            throw new IllegalQuantitativeUnitException();
        this.unit = unit;
    }

    @Column(name="number_value")
    public Number getValue(){
        return super.value;
    }
    protected void setValue( Number newValue){
        super.value = newValue;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  QuantitativeFact)) return false;
        QuantitativeFact qFact = (QuantitativeFact) obj;

        // TODO: implementare una logica diversa senza richiamare super.equals(), in modo da considerare uguali
        // due numeri con valori diversi ma che convertiti alla stessa unitá di misura risultano di egual valore.
        if(super.equals(qFact) && unit.equals(qFact.unit))
            return true;
        else return false;
    }

    public class IllegalQuantitativeUnitException extends IllegalValueException {}
}
