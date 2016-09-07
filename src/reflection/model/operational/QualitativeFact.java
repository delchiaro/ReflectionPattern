/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.operational;

import reflection.model.knowledge.Phenomenon;
import reflection.model.knowledge.QualitativeType;

import javax.persistence.*;

@Entity
@DiscriminatorValue("QUALITATIVE")
public class QualitativeFact extends Fact<Phenomenon> {

    protected QualitativeFact(){}
    public QualitativeFact(QualitativeType factType, Phenomenon value) throws IllegalQualitativePhenomenonException {
        super(factType, value);
        if(factType.isPhenomenonLegal(value) == false )
            throw new IllegalQualitativePhenomenonException();
    }


    // TODO: Doesn't work.. solve!
    @ManyToOne(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @JoinColumn(name = "qualitative_value_phenom_id")
    public Phenomenon getValue() {
        return super.value;
    }
    protected void setValue(Phenomenon value) throws IllegalQualitativePhenomenonException {
        if( ((QualitativeType)(super.getType())).isPhenomenonLegal(value) == false )
            throw new IllegalQualitativePhenomenonException();
        super.value = value;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  QualitativeFact)) return false;
        return super.equals(obj);
    }

    public class IllegalQualitativePhenomenonException extends IllegalValueException {}


}
