package reflectionPattern.dataGeneration;

public class Range {
    public class MinimumValueException extends  Exception {};
    public class InfSupValueException extends  Exception {};

    private static final int DEFAULT_MINIMUM = 0;
    private int minimum = DEFAULT_MINIMUM;
    private int inf;
    private int sup;


    public Range(){};

    /**
     * Create a new range [inf,sup] with default interval inf=sup =minimum, and set the minimum legal value for future interval bound change.
     * @param minimum this parameter set the minimum legal value that inf and sup can assume.
     */
    public Range(int minimum) {
        this.minimum = minimum;
        inf = sup = minimum;
    }

    /**
     * Create a new range [inf,sup] with inf=minimum, and set the minimum legal value for future interval bound change.
     * @param minimum this parameter set the minimum legal value that inf and sup can assume.
     * @param sup the superior bound of the interval, sup must be greater than minimum.
     */
    public Range(int minimum, int sup) throws MinimumValueException, InfSupValueException {
        this.minimum = this.inf = minimum;
        setSup(sup);
    }

    /**
     * Create a new range [inf,sup], and set the minimum legal value for future interval bound change.
     * @param minimum this parameter set the minimum legal value that inf and sup can assume.
     * @param inf the inferior bound of the interval, inf must be greater than minimum.
     * @param sup the superior bound of the interval, sup must be greater than inf.
     * @throws MinimumValueException thrown if inf<minimum
     * @throws InfSupValueException thrown if sup<inf
     */
    public Range(int minimum, int inf, int sup) throws MinimumValueException, InfSupValueException {
        this.minimum = minimum;
        setInfSup(inf, sup);
    }

    public void setInf(int inf_value) throws MinimumValueException, InfSupValueException {
        inf = inf_value;
        validate();
    }
    public void setSup(int sup_value) throws MinimumValueException, InfSupValueException {
        sup = sup_value;
        validate();
    }
    public void setInfSup(int inf_val, int sup_val) throws InfSupValueException, MinimumValueException {
        inf = inf_val;
        sup = sup_val;
        validateMinimum();
        validateInfSup();
    }
    public void setFixedInfSup(int inf_sup_value) throws MinimumValueException {
        sup = inf = inf_sup_value;
        validateMinimum();
    }

    private void validateMinimum() throws MinimumValueException {
        if(inf<minimum) throw new MinimumValueException();
    }
    private void validateInfSup() throws InfSupValueException {
        if(inf>sup) throw new InfSupValueException();
    }
    private void validate() throws InfSupValueException, MinimumValueException {
        validateMinimum();
        validateInfSup();
    }

    public int inf(){
        return inf;
    }
    public int sup(){
        return sup;
    }

}
