/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;


import com.sun.istack.internal.NotNull;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("TEXTUAL")
public class TextualType extends FactType {

    protected  TextualType()  {}
    public     TextualType(@NotNull String typeName)   { super(typeName); }






 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/


    @Override public boolean equals(Object obj) {
        if(this==obj) return true;
        if(! (obj instanceof TextualType)) return false;
        return super.equals(obj);
    }




}
