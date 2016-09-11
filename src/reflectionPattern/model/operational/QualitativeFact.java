/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.operational;

import reflection.model.knowledge.Phenomenon;
import reflection.model.knowledge.QualitativeType;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("QUALITATIVE")
public class QualitativeFact extends Fact {

    @ManyToOne (fetch=FetchType.LAZY)
    private Phenomenon phenomenon;

    protected QualitativeFact(){}
    public QualitativeFact(QualitativeType factType, Phenomenon phen) throws IllegalQualitativePhenomenonException {
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
        if(!(obj instanceof  QualitativeFact)) return false;
        return super.equals(obj);
    }

    public class IllegalQualitativePhenomenonException extends IllegalValueException {}


    @Override
    public String toString() {
        return super.toString() + ": " + this.phenomenon.getValue();
    }
}
