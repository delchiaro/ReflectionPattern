/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.*;




/* http://stackoverflow.com/questions/463349/jpa-eager-fetch-does-not-join

JPA doesn't provide any specification on mapping annotations to select fetch strategy.
In general, related entities can be fetched in any one of the ways given below

    SELECT => one query for root entities + one query for related mapped entity/collection of each root entity = (n+1) queries
    SUBSELECT => one query for root entities + second query for related mapped entity/collection of all root entities retrieved in first query = 2 queries
    JOIN => one query to fetch both root entities and all of their mapped entity/collection = 1 query

So SELECT and JOIN are two extremes and SUBSELECT falls in between. One can choose suitable strategy based on her/his domain model.

By default SELECT is used by both JPA/EclipseLink and Hibernate. This can be overridden by using

@Fetch(FetchMode.JOIN)
@Fetch(FetchMode.SUBSELECT)

in Hibernate. It also allows to set SELECT mode explicitly using @Fetch(FetchMode.SELECT) which can be tuned by using batch size e.g. @BatchSize(size=10)

Corresponding annotations in EclipseLink are

@JoinFetch
@BatchFetch


 */

@Entity
@DiscriminatorValue("COMPOSITE")
public class CompositeType extends FactType {



    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL /* , mappedBy = "parent_type",*/ )//LAZY = non carico subito tutti i figli
    @JoinColumn(name="parent_type")
    private Set<FactType> _childTypes = new HashSet<>();

    protected CompositeType() {}
    public CompositeType(String typeName) {
        super(typeName);

    }



    public void addChild(@NotNull FactType childType ){
        this._childTypes.add(childType);
        //childType.setFatherType(this);
        pushAncestorsToNewChild(childType);
    }


    private void pushAncestorsToNewChild(@NotNull FactType child)
    {
        // When new Child is added to this composite, I add all my ancestors and myself to the Child ancestors.

        // The FIRST ELEMENT OF THE LIST of is the direct ancestor of mySelf (my FATHER).
        // The LAST ELEMENT OF THE LIST is the most far ancestor of mySelf (my family founder, ROOT COMPOSITE).

        for( CompositeType ancestor : getAncestors())
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
        if(child instanceof CompositeType)
        {
            CompositeType compChild = (CompositeType) child;
            if(compChild._childTypes.size() > 0) // (redundant check)
                compChild.updateChildsAncestors(compChild.getAncestors());
            // if the new child added is a composite, and if he has childs, I update these child about their ancestors
            // (from grandfather to the root: this == grandfather of the childs of compChild)
        }
    }

    private void updateChildsAncestors(List<CompositeType> newAncestors)
    {
        for(FactType child : _childTypes)
        {
            // Add the new anchestor at the end of the child's ancestors list:
            child.appendAllAncestors( newAncestors );

            if(child instanceof CompositeType)
                ((CompositeType) child).updateChildsAncestors(newAncestors);
        }
    }


    public Set<FactType> getChildTypes() {
        return Collections.unmodifiableSet(_childTypes);
    }


    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
        if(!(obj instanceof  CompositeType)) return false;
        CompositeType head = (CompositeType)obj;

        if( super.equals(head) && head._childTypes.size() == this._childTypes.size())
        {
            for (FactType childType : head._childTypes)
            {
                if(!(this._childTypes.contains(childType))) // Set.contains() usa equals() dell'oggetto per capire se sono uguali, abbiamo quindi una ricorsivita' nel caso di figli CompositeType.
                    return false;

            }
            return true;
        }
        else return false;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        //result = 31 * result + (_childTypes != null ? _childTypes.hashCode() : 0); // REMOVED
        return result;
    }

    public static Set<FactType> explorer(@NotNull CompositeType head) {
        Set<FactType> list = new HashSet<>();

        for (FactType childType : head._childTypes)
        {
            if(childType.getClass() == CompositeType.class)
                list.addAll(explorer((CompositeType)childType));
            else
                list.add(childType);
        }
        return list;
    }


}
