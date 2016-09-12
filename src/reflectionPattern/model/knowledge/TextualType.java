/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TEXTUAL")
public class TextualType extends FactType {

    protected TextualType(){}
    public TextualType(String typeName) {
        super(typeName);
    }


    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof  TextualType)) return false;
        TextualType txObj = (TextualType)obj;
        if(super.equals(txObj))
            return true;
        else return false;
    }
}
