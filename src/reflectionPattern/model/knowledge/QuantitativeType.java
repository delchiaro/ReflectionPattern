/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.quantity.Unit;

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
        this.legalUnits = legalUnits;
    }

    public QuantitativeType ( @NotNull QuantitativeType copy)
    {
        super(copy);
        this.legalUnits = new HashSet<>();
        for( Unit c : copy.legalUnits)
            legalUnits.add( c.deepCopy() );
    }
    @Override
    public FactType deepCopy() {
        return new QuantitativeType(this);
    }



    @ManyToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="QuantitativeType_legalUnits")
    public Set<Unit>  getLegalUnits()                { return Collections.unmodifiableSet(legalUnits); }
    public void       setLegalUnits(Set<Unit> units) { this.legalUnits = units; }




 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/

    @Override
    public String toString() {
        String ret = super.toString();
        int size = legalUnits.size();
        if(size > 0)
        {
            ret += " - legals {";
            int i = 0;
            for (Unit p : legalUnits)
                ret += p.getName() + (++i == size ? "}" : ", ");
        }
        return ret;
    }

    @Override
    public void acceptVisitor(IFactTypeVisitor visitor) {
        visitor.visit(this);
    }


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
