/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import utility.composite.ComponentManager;
import utility.composite.IComponent;
import utility.visitor.Visitable;

import javax.persistence.*;

@Access(AccessType.PROPERTY)
@Entity
public abstract class FactType implements IComponent<CompositeType>, Visitable<IFactTypeVisitor> {



    @Transient
    private ComponentManager<FactType, CompositeType> componentManager = new ComponentManager<>(this);
    protected Long id;
    private String typeName;



    protected FactType() {
        this.typeName=null;
    }
    public FactType(@NotNull  String typeName){
        this.typeName = typeName;
    }
    public FactType(FactType copy) {
        this.typeName = copy.getTypeName();
    }
    public abstract FactType clone();



    @ManyToOne (fetch=FetchType.LAZY, optional = true)
    @JoinColumn(name="parent_fk")
    //@Fetch(FetchMode.SELECT)
    @Override
    public CompositeType    getParent()                  { return componentManager.getParent(); }
    public void          setParent(CompositeType parent) { componentManager.setParent(parent);  }



    @Column(name = "id")
    @Id @GeneratedValue
    public    Long  getId()         { return id; }
    protected void  setId(Long id)  { this.id = id; }




    @Column(name = "NAME")
    public  String getTypeName()             { return typeName; }
    public  void   setTypeName(String name ) { this.typeName = name; }





/* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/

    @Override public String toString() {
        return this.getTypeName();
    }


    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + typeName.hashCode();
        // TODO: include parentType in the hashcode? I don't think it's good.. that properties is there only for hibernate mapping (for the DAO.. see comments)
        return result;
    }



    private static final EqualCheck defaultEqualCheck = EqualCheck.pk_if_exists_and_deeep;
    //private static final EqualCheck defaultEqualCheck = EqualCheck.pk_if_exists;

    @Override
    public boolean equals(Object obj) {
        return equals(obj, defaultEqualCheck);
    }

    public boolean equals(Object obj, EqualCheck equalCheck) {
        if(this==obj) return true;
        if(obj == null) return false;
        if ( !(obj instanceof FactType) ) return false;

        FactType fType = (FactType) obj;

        switch(equalCheck)
        {
            case pk_forced:              return equals_pkForcedCheck(fType);
            case pk_if_exists:           return equals_pkIfExistsCheck(fType);
            case deep:                   return equals_deepCheck(fType);
            case pk_if_exists_and_deeep: return equals_pkIfExistsAndDeepCheck(fType);
            case pk_forced_and_deeep:    return equals_pkForcedAndDeepCheck(fType);
            default: return false;
        }
    }


    public enum EqualCheck { pk_forced, pk_if_exists, deep, pk_if_exists_and_deeep, pk_forced_and_deeep}


 /* EQU
         TRUTH TABLE:        (where D is the result of the deep check)

                        |   pk            pk if   pkIfExists   deep      pk forced
        PK1     PK2     | forced    --    exists   and deep    only      and deep
        ----------------|-----------------------------------------------------------
        null    null    |   F       D       D         D         D          F
        null    A       |   F       F       D         D         D          F
        A       null    |   F       F       D         D         D          F
        A       A       |   T       T       T         D         D          D
        A       B       |   F       F       F         F         D          F
        ----------------|-----------------------------------------------------------

     */

    /**
     * Check if both objects have an instance of the primary key (id). If they have, it compares the two pk values and return
     * the compare results. If they don't, it return false.
     * @param fType
     * @return
     */
    private boolean equals_pkForcedCheck(FactType fType) {
        if(this.id != null && fType.id != null)
            return this.id.equals(fType.id);
        else return false;
    }

    /**
     * Check the value of the properties, excluding the primary key value (id).
     * @param fType
     * @return
     */
    private boolean equals_deepCheck(FactType fType) {
        if(this.typeName == null)
            return fType.typeName == null; // true if both are null, false otherwise
        else return this.typeName.equals(fType.typeName);
    }

    /**
     * Check if both objects have an instance of the primary key (id). If they have, it compares the two pk values and return
     * the compare results. If they don't, it return the result of a deep check.
     * @param fType
     * @return
     */
    private boolean equals_pkIfExistsCheck(FactType fType) {
        if(this.id != null && fType.id != null)
            return this.id.equals(fType.id);
        else return equals_deepCheck(fType);
    }

    /**
     * Deep equals check and, if exists, pk check.
     * If pk does not exists, returns deep equals check.
     * If pk exists returns (deep equals check) && (pk equals check)
     * @param fType
     * @return
     */
    private boolean equals_pkIfExistsAndDeepCheck(FactType fType) {
        boolean pkCheck = true;
        if(this.id != null  &&  fType.id != null)
            pkCheck = this.id.equals(fType.id);
        return pkCheck && equals_deepCheck(fType);
    }

    private boolean equals_pkForcedAndDeepCheck(FactType fType) {
        return equals_pkForcedCheck(fType) && equals_deepCheck(fType);
    }


}
