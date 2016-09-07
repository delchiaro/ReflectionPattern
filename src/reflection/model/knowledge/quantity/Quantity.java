package reflection.model.knowledge.quantity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by nagash on 02/09/16.
 */

@Embeddable
public class Quantity {
    @ManyToOne
    private Unit    unit;

    @Column
    private Number  value;

    protected Quantity() {}
    public Quantity(Number value, Unit unit){
        this.unit=unit;
        this.value=value;
    }

    public Number getValue(){
        return value;
    }
    public Unit getUnit(){
        return unit;
    }

    public Quantity convert(Unit toUnit) throws ImpossibleConversionException {
        return UnitConverter.converter().convert(this, toUnit);
    }

}
