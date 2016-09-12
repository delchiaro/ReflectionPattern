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
@DiscriminatorValue("QUANTITATIVE")
public class QuantitativeType extends FactType {
    //private ImmutableSet<Unit> legalUnits;


    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Unit> legalUnits;

    protected QuantitativeType() {}

    public QuantitativeType(String typeName) {
        super(typeName);
        this.legalUnits = new HashSet<>();
    }
    public QuantitativeType(String typeName, @NotNull Set<Unit> legalUnits) {
        super(typeName);
        this.legalUnits = new HashSet<>(legalUnits);
    }

    public boolean isUnitLegal(Unit unit) {
        if(legalUnits.contains(unit))
            return true;
        else return false;
    }

    public void addLegalUnit(Unit unit) {
        legalUnits.add(unit);
    }
    public Set<Unit> getLegalUnits() {
        return Collections.unmodifiableSet(legalUnits);
    }




    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof  QuantitativeType)) return false;
        QuantitativeType qtObj = (QuantitativeType)obj;
        if( super.equals(qtObj) &&
                qtObj.getLegalUnits().size() == this.getLegalUnits().size()  )
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
