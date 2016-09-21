package reflectionPattern.IO.compositeAdapter;

import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;

import java.util.Set;

/**
 * Created by nagash on 06/09/16.
 */
public class FactTypeCompositeAdapter extends CompositeAdapter<FactType,String> {

    public FactTypeCompositeAdapter(FactType adaptedObject) {
        super(adaptedObject);
    }

    // TODO: How can I enforce this behaviour from Abstract superclass??
    @Override
    protected CompositeAdapter newCompositeAdapter(FactType adaptedObject) {
        return new FactTypeCompositeAdapter(adaptedObject);
    }

    @Override
    protected Set<FactType> getChilds() {
        if(getAdapted() instanceof CompositeType)
            return ((CompositeType) getAdapted() ).getChilds();
        else return null;
    }

    @Override
    public String getValue() {
        return getAdapted().getTypeName();
    }
}
