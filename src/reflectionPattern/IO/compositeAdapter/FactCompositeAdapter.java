//package reflectionPattern.IO.compositeAdapter;
//
//import reflectionPattern.model.operational.*;
//
//import java.util.Set;
//
///**
// * Created by nagash on 06/09/16.
// */
//public class FactCompositeAdapter extends CompositeAdapter<Fact, String> {
//
//
//    public FactCompositeAdapter(Fact adaptedObject) {
//        super(adaptedObject);
//    }
//
//
//    // TODO: How can I enforce this behaviour from Abstract superclass??
//    @Override
//    protected CompositeAdapter newCompositeAdapter(Fact adaptedObject) {
//        return new FactCompositeAdapter(adaptedObject);
//    }
//
//    @Override
//    protected Set<Fact> getChilds() {
//        if(getAdapted() instanceof CompositeFact)
//            return ((CompositeFact)getAdapted()).getChilds();
//        else return null;
//    }
//
//    @Override
//    public String getValue() {
//        if(getAdapted() instanceof CompositeFact) // evito di stampare ": null" per ogni CompositeFact (getValue() restituisce null)
//            return getAdapted().getType().getTypeName();
//
//        else return getAdapted().toString();
//
//    }
//}
