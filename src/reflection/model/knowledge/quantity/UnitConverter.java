package reflection.model.knowledge.quantity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Map;

/**
 * Created by nagash on 02/09/16.
 */
class UnitConverter {
    private static UnitConverter _converter=null;

    static UnitConverter converter(){
        if(_converter==null)
            _converter=new UnitConverter();
        return _converter;
    }


    Table<Unit, Unit, Number> conversionTable;

    private UnitConverter() {
        conversionTable = HashBasedTable.create();
    }

    void newConversionRatio(Unit fromUnit, Unit toUnit, Number ratio) throws ImpossibleConversionException {
        if(ratio.doubleValue()==0)
            throw new ImpossibleConversionException();

        converter().conversionTable.put(fromUnit, toUnit, ratio);
        converter().conversionTable.put(toUnit, fromUnit, 1/ratio.doubleValue());
    }


    Quantity convert(Quantity quantity, Unit toUnit) throws ImpossibleConversionException {
        return new Quantity(convert(quantity.getValue(), quantity.getUnit(), toUnit), toUnit);
    }
    double convert(Number value, Unit fromUnit, Unit toUnit) throws ImpossibleConversionException
    {
        Number conversionRatio = converter().conversionTable.get(fromUnit, toUnit);
        if(conversionRatio==null)
            throw new ImpossibleConversionException();
        else return value.doubleValue()*conversionRatio.doubleValue();
    }


    public Map<Unit, Number> getConversions(Unit unit) {
        return conversionTable.row(unit);
    }


}
