/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.modelALS.operational;

import com.sun.istack.internal.NotNull;
import reflectionPattern.modelALS.knowledge.Phenomenon;
import reflectionPattern.modelALS.knowledge.QualitativeType;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("QUALITATIVE")
public class QualitativeFact extends Fact {

    private Phenomenon phenomenon;

    protected   QualitativeFact () {}
    public      QualitativeFact (@NotNull QualitativeType factType, @NotNull Phenomenon phen) throws IllegalQualitativePhenomenonException {
        super(factType);
        this.phenomenon = phen;
        if(factType.isPhenomenonLegal(phen) == false )
            throw new IllegalQualitativePhenomenonException();
    }



    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="phenomenon_id")
    public Phenomenon getPhenomenon ()                { return this.phenomenon; }
    protected   void        setPhenomenon (Phenomenon phen) { this.phenomenon = phen; } // for hibernate, no check legal phen check!



 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/



    @Override
    public String toString() {
        return super.toString() + ": " + this.phenomenon.getValue();
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + phenomenon.hashCode();
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
        if(this==obj) return true;
        if(!(obj instanceof QualitativeFact)) return false;

        QualitativeFact qf = (QualitativeFact)obj;
        if(this.phenomenon == null)
            return qf.phenomenon == null;
        else return this.phenomenon.equals(qf.phenomenon);
    }


    public class IllegalQualitativePhenomenonException extends IllegalValueException {}


}
