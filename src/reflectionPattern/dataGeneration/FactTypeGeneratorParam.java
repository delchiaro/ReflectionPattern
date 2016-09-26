package reflectionPattern.dataGeneration;

import reflectionPattern.model.knowledge.Phenomenon;

public class FactTypeGeneratorParam {
    public class IllegalValueException extends Exception {}
    private final static int DEFAULT_MAX = 5;

    // depth.sup == 0      --> generate only the root FactType.
    // rootChilds.inf > 0  --> the root FactType is forced to be a CompositeType

    // It's not legal to set:    depth.sup=0  and  rootChilds.inf>0
    // Infact with these parameters we force to have only the root element, and we force the root element to have
    // at least 1 child. This is impossible.

    //                      Range(min, inf, sup)
    private Range rootChilds  = new Range(0,    0,  DEFAULT_MAX);
    private Range compChilds  = new Range(1,    1,  DEFAULT_MAX); //every compositeType must have at least 1 child
    private Range depth       = new Range(0,    1,  DEFAULT_MAX);
    private Range phenoms     = new Range(1,        DEFAULT_MAX);
    private Range units       = new Range(1,        DEFAULT_MAX);

    private Class subPhenomenon = Phenomenon.class;

    private boolean leafOnlyAtLowerLevel = false;


    public FactTypeGeneratorParam() throws Range.InfSupValueException, Range.MinimumValueException {}


    public Range rootChildsRange() { return rootChilds; }
    public Range compChildsRange() { return compChilds; }
    public Range depthRange()      { return depth; }
    public Range phenomsRange()    { return phenoms; }
    public Range unitsRange()      { return units; }
    public Class getSubPhenomenon(){ return subPhenomenon; }


    public void setSubPhenomenon(Class<Phenomenon> SubPhenClass ) {
        this.subPhenomenon = SubPhenClass;
    }
    public void setLeafOnlyAtLowerLevel(boolean value) {
        this.leafOnlyAtLowerLevel = value;
    }
    public boolean isLeafOnlyAtLowerLevel(){
        return this.leafOnlyAtLowerLevel;
    }
}
