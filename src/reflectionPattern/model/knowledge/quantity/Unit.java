package reflectionPattern.model.knowledge.quantity;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by nagash on 02/09/16.
 */


@Entity
@Table(name = "UNIT")
public class Unit {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;


    private String name;
    private String symbol;

    //for persistency
    protected Unit() {
        name = symbol = null;
    }

    public Unit(@NotNull String name, @NotNull String symbol){
        this.name=name;
        this.symbol=symbol;
    }

    @Column(name = "NAME")
    public String getName(){
        return name;
    }

    @Column(name = "SYMBOL")
    public String getSymbol(){
        return symbol;
    }

    public void addConversionRatio(Unit toUnit, Number ratio) throws ImpossibleConversionException {
        UnitConverter.converter().newConversionRatio(this, toUnit, ratio );
    }
    public Map<Unit, Number> getConversions() {
        return UnitConverter.converter().getConversions(this);
    }


    @Override
    public boolean equals(Object obj) {
        if( obj instanceof Unit)
        {
            Unit unit = (Unit) obj;
            if(unit.getName().equals(this.getName()) && unit.getSymbol().equals(this.getSymbol())  )
                return true;
            else return false;
        }
        else return false;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }
}
