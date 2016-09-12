/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import reflectionPattern.model.knowledge.TextualType;

import javax.persistence.*;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("TEXTUAL")
public class TextualFact extends Fact {


    protected TextualFact(){}

    public TextualFact(TextualType type, String value) {
        super(type);
    }

    @Column(name="textual_value")
    private String value;

    public String getValue() {
        return value;
    }
//    protected void setValue(String value) {
//        value = value;
//    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  TextualFact)) return false;
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + this.value;
    }

}
