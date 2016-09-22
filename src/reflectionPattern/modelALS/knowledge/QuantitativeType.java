/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.modelALS.knowledge;

import com.sun.istack.internal.NotNull;
import reflectionPattern.modelALS.knowledge.quantity.Unit;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("QUANTITATIVE")
public class QuantitativeType extends FactType {

    private Set<Unit> legalUnits;



    protected   QuantitativeType  () {}
    public      QuantitativeType  (@NotNull String typeName) {
        super(typeName);
        this.legalUnits = new HashSet<>();
    }
    public      QuantitativeType  (@NotNull String typeName, @NotNull Set<Unit> legalUnits) {
        super(typeName);
        this.legalUnits = new HashSet<>(legalUnits);
    }




    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="QuantitativeType_legalUnits")
    public Set<Unit>  getLegalUnits()                { return Collections.unmodifiableSet(legalUnits); }
    public void       setLegalUnits(Set<Unit> units) { this.legalUnits = units; }





 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/



    public boolean isUnitLegal  (Unit unit) {
        if(legalUnits.contains(unit))
            return true;
        else return false;
    }
    public void    addLegalUnit (@NotNull Unit unit) {
        legalUnits.add(unit);
    }



    @Override public int hashCode() {
        int result = super.hashCode();
        //result = 31 * result + (legalUnits != null ? legalUnits.hashCode() : 0);
        return result;
    }

    @Override public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
        if(! (obj instanceof QuantitativeType)) return false;
        QuantitativeType qtObj = (QuantitativeType)obj;
        if( qtObj.getLegalUnits().size() == this.getLegalUnits().size()  )
        {
            for (Unit unit : qtObj.getLegalUnits())
            {
                if( ! this.getLegalUnits().contains(unit) ) // Set<QuantitativeType>.contains(unit) usa  unit.equals() (equivalenza) non operatore == (identita')
                    return false;
            }
            return true;
        }
        else return false;
    }

}
