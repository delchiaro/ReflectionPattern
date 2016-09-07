/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.knowledge;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


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
