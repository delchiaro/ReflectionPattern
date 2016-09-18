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
            pushAncestorsToNewChild(childFact);

        }
    }


    private void pushAncestorsToNewChild(@NotNull Fact child)
    {
        // When new Child is added to this composite, I add all my ancestors and myself to the Child ancestors.

        // The FIRST ELEMENT OF THE LIST of is the direct ancestor of mySelf (my FATHER).
        // The LAST ELEMENT OF THE LIST is the most far ancestor of mySelf (my family founder, ROOT COMPOSITE).

        for( CompositeFact ancestor : getAncestors())
            child.addFirstAncestor(ancestor); // add at the beginning of the list
        child.addFirstAncestor(this);



        // If the new child is a CompositeType, there could be a problem:
        // the compositeChild could have already added some child, so I have to tell to the child of the compositeChild
        // that I am the father of his father (grandfather), and I have to tell them about all my ancestors.

        // NB: a child can have only 1 father, so the child of the compChild didn't know anything about their grandfather
        // because if I'm adding now the compChild to a composition, it means that compChild never had a father until now,
        // and so the childs of the compChild never had idea of what was their's grandfather and other ancestors were.
        // They only know who was their father...
        // So I have simply to add to their ancestors all the ancestors of the father, which until now were unknown.
        if(child instanceof CompositeFact)
        {
            CompositeFact compChild = (CompositeFact) child;
            if(compChild._childFacts.size() > 0) // (redundant check)
                compChild.updateChildsAncestors(compChild.getAncestors());
            // if the new child added is a composite, and if he has childs, I update these child about their ancestors
            // (from grandfather to the root: this == grandfather of the childs of compChild)
        }
    }

    private void updateChildsAncestors(List<CompositeFact> newAncestors)
    {
        for(Fact child : _childFacts)
        {
            // Add the new anchestor at the end of the child's ancestors list:
            child.appendAllAncestors( newAncestors );

            if(child instanceof CompositeFact)
                ((CompositeFact) child).updateChildsAncestors(newAncestors);
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
