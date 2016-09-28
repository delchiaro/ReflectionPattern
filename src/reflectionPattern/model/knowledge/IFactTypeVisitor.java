package reflectionPattern.model.knowledge;

import utility.visitor.IVisitor;

/**
 * Created by nagash on 27/09/16.
 */
public interface IFactTypeVisitor extends IVisitor {
    @Override void visit(Object object);
    public void visit(FactType type);
    public void visit(CompositeType type);
    public void visit(QualitativeType type);
    public void visit(QuantitativeType type);
    public void visit(TextualType type);
}
