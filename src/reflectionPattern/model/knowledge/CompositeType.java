/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;
import reflectionPattern.utility.compositeWithAncestors.CompositeManagerALS;
import reflectionPattern.utility.compositeWithAncestors.ICompositeALS;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


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
@Access(AccessType.PROPERTY)
@Entity
@DiscriminatorValue("COMPOSITE")
public class CompositeType extends FactType implements ICompositeALS<CompositeType, FactType> {


    private CompositeManagerALS<CompositeType, FactType> compositeManager = new CompositeManagerALS<>(this);


    protected CompositeType () {}
    public    CompositeType (String typeName) { super(typeName); }





    // use mappedBy ="---" with the same column name of @joinColumn specified in the @ManyToOne side
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
    @Override
    public    Set<FactType> getChilds ()                     { return compositeManager.getChilds(); }
    protected void          setChilds (Set<FactType> childs) { compositeManager.setChilds(childs);  }
    @Override public void   addChild (@NotNull FactType childType ){ this.compositeManager.addChild(childType); }




 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/



    @Override public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
        if(!(obj instanceof CompositeType)) return false;
        CompositeType head = (CompositeType)obj;

        if( super.equals(head) && head.getChilds().size() == this.getChilds().size())
        {
            for (FactType childType : head.getChilds())
            {
                if(!(this.getChilds().contains(childType))) // Set.contains() usa equals() dell'oggetto per capire se sono uguali, abbiamo quindi una ricorsivita' nel caso di figli CompositeType.
                    return false;
            }
            return true;
        }
        else return false;
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        //result = 31 * result + (_childTypes != null ? _childTypes.hashCode() : 0); // REMOVED
        return result;
    }

    public static Set<FactType> offsprings(@NotNull CompositeType head) {
        Set<FactType> list = new HashSet<>();

        for (FactType childType : head.getChilds())
        {
            if(childType.getClass() == CompositeType.class)
                list.addAll(offsprings((CompositeType)childType));
            else
                list.add(childType);
        }
        return list;
    }

}
