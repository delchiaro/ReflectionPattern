/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.Phenomenon;
import reflectionPattern.model.knowledge.QualitativeType;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("QUALITATIVE")
public class QualitativeFact extends Fact {

    @ManyToOne (fetch=FetchType.LAZY)
    private Phenomenon phenomenon;

    protected QualitativeFact(){}
    public QualitativeFact(@NotNull QualitativeType factType, @NotNull  Phenomenon phen) throws IllegalQualitativePhenomenonException {
        super(factType);
        this.phenomenon = phen;
        if(factType.isPhenomenonLegal(phen) == false )
            throw new IllegalQualitativePhenomenonException();
    }


    public Phenomenon getPhenomenon() {
        return phenomenon;
    }
//    protected void setPhenomenon(Phenomenon value) throws IllegalQualitativePhenomenonException {
//        if( ((QualitativeType)(super.getType())).isPhenomenonLegal(value) == false )
//            throw new IllegalQualitativePhenomenonException();
//        phenomenon = value;
//    }


    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
        if(this==obj) return true;
        if(!(obj instanceof  QualitativeFact)) return false;

        QualitativeFact qf = (QualitativeFact)obj;
        if(this.phenomenon == null)
            return qf.phenomenon == null;
        else return this.phenomenon.equals(qf.phenomenon);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + phenomenon.hashCode();
        return result;
    }

    public class IllegalQualitativePhenomenonException extends IllegalValueException {}


    @Override
    public String toString() {
        return super.toString() + ": " + this.phenomenon.getValue();
    }
}
