/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.operational;

import reflection.model.knowledge.CompositeType;
import reflection.model.knowledge.FactType;

import javax.persistence.*;
import java.util.*;
import java.util.function.BooleanSupplier;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("COMPOSITE")
public class CompositeFact extends Fact {

    public class IllegalFactTypeException extends Exception {}

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.PERSIST /* , mappedBy = "parent_fact",*/ )//EAGER = carico tutti i figli subito!
    @JoinColumn(name="parent_fact")
    private Set<Fact> _childFacts = new HashSet<>();

    @ElementCollection
    @MapKeyColumn(name="typeCheckKey")
    @Column(name="typeCheckValue")
    //@CollectionTable(name="example_attributes", joinColumns=@JoinColumn(name="example_id"))
    private Map<FactType, Integer> compositionTypeCheck = new HashMap<>();


    protected CompositeFact() {}
    public CompositeFact(CompositeType compType) {
        super(compType);
        for(FactType type : compType.getChildTypes())
            compositionTypeCheck.put(type, 0);
    }


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
        }
    }
    public Set<Fact> getChildFacts() {
        return Collections.unmodifiableSet(_childFacts);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  CompositeFact)) return false;
        CompositeFact head = (CompositeFact)obj;

        // Don't call super.equals() with CompositeFact because generics T = Void, so super.value is null!
        if( this.getType().equals(head.getType()) && this.getId().equals(head.getId()) && head._childFacts.size() == this._childFacts.size())
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
    public String toString() {
        return super.toString();
    }
}
