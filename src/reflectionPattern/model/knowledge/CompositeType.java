/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collections;
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

    public void addChild(FactType childType ){
        this._childTypes.add(childType);
        //childType.setFatherType(this);
    }
    public Set<FactType> getChildTypes() {
        return Collections.unmodifiableSet(_childTypes);
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  CompositeType)) return false;
        CompositeType head = (CompositeType)obj;

        if( super.equals(head) && head.getChildTypes().size() == this.getChildTypes().size())
        {
            for (FactType childType : head.getChildTypes())
            {
                if(!(this.getChildTypes().contains(childType))) // Set.contains() usa equals() dell'oggetto per capire se sono uguali, abbiamo quindi una ricorsivita' nel caso di figli CompositeType.
                    return false;
            }
            return true;
        }
        else return false;

    }

    public static Set<FactType> explorer(CompositeType head) {
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
