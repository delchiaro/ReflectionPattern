/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.QuantitativeType;
import reflectionPattern.model.knowledge.quantity.Quantity;
import reflectionPattern.model.knowledge.quantity.Unit;

import javax.persistence.*;


@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("QUANTITATIVE")
public class QuantitativeFact extends Fact {


    private Quantity quantity = null;


    protected QuantitativeFact (){}
    public    QuantitativeFact (@NotNull QuantitativeType factType) {
        super(factType);
    }
    public    QuantitativeFact (@NotNull QuantitativeType factType, @NotNull Number value, @NotNull Unit measurementUnit) throws IllegalQuantitativeUnitException {
        this(factType, new Quantity(value, measurementUnit));

    }
    public    QuantitativeFact (@NotNull QuantitativeType factType, @NotNull Quantity quantity) throws IllegalQuantitativeUnitException {
        super(factType);
        if(factType.isUnitLegal(quantity.getUnit()) == false )
            throw new IllegalQuantitativeUnitException();
        else this.quantity = quantity;

    }



    @Embedded
    public    Quantity getQuantity ()                 { return this.quantity; }
    protected void     setQuantity(Quantity quant)    { this.quantity = quant; } // NO  LEGAL UNIT CHECK!! Only for hibernate
    public    void     assignQuantity(Quantity quant) {
        if( ((QuantitativeType)getType()).getLegalUnits().contains(quant))
            this.quantity = quant;
        else; // throw exception
    }





 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/
     @Override
     public void acceptVisitor(IFactVisitor visitor) {
         visitor.visit(this);
     }


    @Override public String toString() {
        return super.toString() + ": " + (quantity != null ? quantity.getValue().toString() : "");
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
        if(!(obj instanceof QuantitativeFact)) return false;
        QuantitativeFact qFact = (QuantitativeFact) obj;

        // TODO: implementare una logica diversa senza richiamare super.equals(), in modo da considerare uguali
        // due numeri con valori diversi ma che convertiti alla stessa unitá di misura risultano di egual valore.
        if(this.quantity == null)
            return (qFact.quantity == this.quantity) && super.equals(obj);
        else return super.equals(obj) && this.quantity.equals(qFact.quantity);
    }


    public class IllegalQuantitativeUnitException extends IllegalValueException {}



}
