package reflectionPattern.model.operational;

import utility.visitor.IVisitor;

/**
 * Created by nagash on 27/09/16.
 */
public interface IFactVisitor extends IVisitor {
    @Override void visit(Object object);
    public void visit(Fact fact);
    public void visit(CompositeFact fact);
    public void visit(QualitativeFact fact);
    public void visit(QuantitativeFact fact);
    public void visit(TextualFact fact);
}
