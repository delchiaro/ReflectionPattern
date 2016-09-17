/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.CompositeType;
import reflectionPattern.model.knowledge.FactType;

import javax.persistence.*;
import java.util.*;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("COMPOSITE")
public class CompositeFact extends Fact {

    public class IllegalFactTypeException extends Exception {}

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST /* , mappedBy = "parent_fact",*/ )//EAGER = carico tutti i figli subito!
    @JoinColumn(name="parent_fact")
    private Set<Fact> _childFacts = new HashSet<>();

    @ElementCollection
    @MapKeyColumn(name="typeCheckKey")
    @Column(name="typeCheckValue")
    //@CollectionTable(name="example_attributes", joinColumns=@JoinColumn(name="example_id"))
    private Map<FactType, Integer> compositionTypeCheck = new HashMap<>();


    protected CompositeFact() {}
    public CompositeFact(@NotNull  CompositeType compType) {
        super(compType);
        for(FactType type : compType.getChildTypes())
            compositionTypeCheck.put(type, 0);
    }


    //if true, we impose that this compositeFact must have max 1 fact of each FactType contained in the associated CompositeType.
    private static final  boolean child_limit = true;
    public void addChild(Fact childFact ) throws IllegalFactTypeException {
        Integer n = compositionTypeCheck.get(childFact.getType());
        if( n == null || (child_limit && n>1)  ) {
            throw new IllegalFactTypeException();
        }
        else
        {
            compositionTypeCheck.put(childFact.getType(), n+1);
            this._childFacts.add(childFact);
            //childFact.setFatherFact(this);

            // ANCESTOR STRATEGY
            // The  LAST ELEMENT OF THE LIST is the direct ancestor of this (FATHER).
            // The FIRST ELEMENT OF THE LIST is the most far ancestor of this (ROOT COMPOSITE).

            // So, the most far ancestor of this CompositeType, become the most far ancestor of the childType, and so on..
            // .. the most near ancestor of this (last element of the list, the father)
            // became the most near ancestor of the child (..will be the grandfather after adding this to the child ancestors)...
            for( CompositeFact ancestor : getAncestors())
                childFact.addAncestor(ancestor); // add at the end of the list.
            childFact.addAncestor(this);
            // .. add at the end of the list, so.. this become the last ancestor of the child (the father).
        }
    }
    public Set<Fact> getChildFacts() {
        return Collections.unmodifiableSet(_childFacts);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false ) return false;
        if(!(obj instanceof  CompositeFact)) return false;
        CompositeFact head = (CompositeFact)obj;

        // Don't call super.equals() with CompositeFact because generics T = Void, so super.value is null!
        if( this.getType().equals(head.getType())
                && head._childFacts.size() == this._childFacts.size())
        {
            for (Fact childType : head._childFacts)
            {
                if(!(this._childFacts.contains(childType))) // Set.contains() usa equals() dell'oggetto per capire se sono uguali, abbiamo quindi una ricorsivita' nel caso di figli CompositeFact.
                    return false;
            }
            return true;
        }
        else return false;
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        // result = 31 * result + (_childFacts != null ? _childFacts.hashCode() : 0); // REMOVED
        //result = 31 * result + (compositionTypeCheck.hashCode() != null ? ;compositionTypeCheck.hashCode() : 0 );
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
