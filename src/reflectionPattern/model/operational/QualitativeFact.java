/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import reflectionPattern.model.knowledge.Phenomenon;
import reflectionPattern.model.knowledge.QualitativeType;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("QUALITATIVE")
public class QualitativeFact extends Fact {

    private static final boolean TO_STRING_SHOW_LEGAL_PHENS = false; // for true value needs to initializate the Type EAGER.
    private Phenomenon phenomenon = null;

    protected   QualitativeFact () {}
    public      QualitativeFact(@NotNull QualitativeType factType) {
        super(factType);
    }
    public      QualitativeFact (@NotNull QualitativeType factType, @NotNull Phenomenon phen) throws IllegalQualitativePhenomenonException {
        super(factType);
        this.phenomenon = phen;
        if(factType.isPhenomenonLegal(phen) == false )
            throw new IllegalQualitativePhenomenonException();
    }



    @ManyToOne (fetch=FetchType.LAZY, cascade = CascadeType.REFRESH )
    @JoinColumn(name="phenomenon_id")
    @LazyToOne(LazyToOneOption.PROXY)
    public     Phenomenon getPhenomenon ()                { return this.phenomenon; }
    protected  void       setPhenomenon(Phenomenon phen) { this.phenomenon = phen; } // for hibernate, no check legal phen check!
    public     void       assignPhenomenon(Phenomenon phen) {
        if( ((QualitativeType)getType()).getLegalPhenomenons().contains(phen) )
            this.phenomenon = phen;
        else; // exception
    }


 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/
     @Override
     public void acceptVisitor(IFactVisitor visitor) {
         visitor.visit(this);
     }



    @Override
    public String toString() {
        String ret;
        if(TO_STRING_SHOW_LEGAL_PHENS)
             ret = super.toString();
        else ret = ((this.getType() != null) ? this.getType().getTypeName() : "");

        ret +=  ": " + (phenomenon != null ? phenomenon.toString() : "");
        return ret;
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (phenomenon != null ? phenomenon.hashCode() : 0);
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
