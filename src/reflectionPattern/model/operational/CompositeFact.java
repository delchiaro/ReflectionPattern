/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;
import utility.composite.CompositeManager;
import utility.composite.IComposite;
import utility.compositeWithAncestors.CompositeManagerALS;
import utility.compositeWithAncestors.ICompositeALS;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("COMPOSITE")
public class CompositeFact extends Fact implements IComposite<CompositeFact, Fact> {

    private static final  boolean child_limit = true;
    //if true, impose that this compositeFact must have max 1 fact of each FactType contained in the associated CompositeType.

    CompositeManager<CompositeFact, Fact> compositeManager = new CompositeManager<>(this);




    protected   CompositeFact () {}
    public      CompositeFact (@NotNull CompositeType compType) {
        super(compType);
//        for(FactType type : compType.getChilds())
//            compositionTypeCheck.put(type, 0);
    }






    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
    @Override
    public      Set<Fact>  getChilds()                 { return compositeManager.getChilds(); }
    protected   void       setChilds(Set<Fact> childs) { this.compositeManager.setChilds(childs);}



//    @ElementCollection
//    @MapKeyColumn(name="typeCheckKey")
//    @Column(name="typeCheckValue")
//    //@CollectionTable(name="example_attributes", joinColumns=@JoinColumn(name="example_id"))
//    private Map<FactType, Integer> compositionTypeCheck = new HashMap<>();





 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/
     @Override
     public void acceptVisitor(IFactVisitor visitor) {
         visitor.visit(this);
     }



    @Override public void addChild(Fact childFact ) {
//        Integer n = compositionTypeCheck.get(childFact.getType());
//        if( n == null || (child_limit && n>1)  ) {
//            // throw new IllegalFactTypeException(); // TODO: throw exception
//        }
//        else
//        {
//            compositionTypeCheck.put(childFact.getType(), n+1);
//            compositeManager.addChild(childFact);
//        }
        compositeManager.addChild(childFact);
    }


    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        // result = 31 * result + (_childFacts != null ? _childFacts.hashCode() : 0); // REMOVED
        //result = 31 * result + (compositionTypeCheck.hashCode() != null ? ;compositionTypeCheck.hashCode() : 0 );
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false ) return false;
        if(!(obj instanceof CompositeFact)) return false;
        CompositeFact head = (CompositeFact)obj;

        // Don't call super.equals() with CompositeFact because generics T = Void, so super.value is null!
        if( this.getType().equals(head.getType())
                && head.getChilds().size() == this.getChilds().size())
        {
            for (Fact childType : head.getChilds())
            {
                if(!(this.getChilds().contains(childType))) // Set.contains() usa equals() dell'oggetto per capire se sono uguali, abbiamo quindi una ricorsivita' nel caso di figli CompositeFact.
                    return false;
            }
            return true;
        }
        else return false;
    }



    public class IllegalFactTypeException extends Exception {}

}
