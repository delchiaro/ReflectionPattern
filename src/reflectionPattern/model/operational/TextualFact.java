/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.TextualType;

import javax.persistence.*;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("TEXTUAL")
public class TextualFact extends Fact {


    protected TextualFact(){}

    public TextualFact(@NotNull TextualType type, @NotNull String value) {
        super(type);
        this.value=value;
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
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
        if(!(obj instanceof  TextualFact)) return false;
        boolean superEquals = super.equals(obj) ;

        TextualFact tf = (TextualFact)obj;

        if(this.value == null)
            return superEquals && this.value == tf.value;
        else return superEquals && this.value.equals(tf.value);
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + this.value;
    }

}
