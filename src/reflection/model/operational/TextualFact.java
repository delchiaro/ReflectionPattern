/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.operational;

import reflection.model.knowledge.TextualType;

import javax.persistence.*;


@Entity
@DiscriminatorValue("TEXTUAL")
public class TextualFact extends Fact<String> {


    protected TextualFact(){}

    public TextualFact(TextualType type, String value) {
        super(type, value);
    }

    @Column(name="textual_value")
    @Access(AccessType.PROPERTY)
    public String getValue() {
        return super.value;
    }
    protected void setValue(String value) {
        super.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  TextualFact)) return false;
        return super.equals(obj);
    }

}
