/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import reflectionPattern.model.knowledge.FactType;
import utility.composite.ComponentManager;
import utility.composite.IComponent;
import utility.compositeWithAncestors.ComponentManagerALS;
import utility.compositeWithAncestors.IComponentALS;
import utility.visitor.Visitable;

import javax.persistence.*;
import java.util.List;


@Entity
@Access(AccessType.PROPERTY)
public abstract class Fact implements IComponent<CompositeFact>, Visitable<IFactVisitor> {



    ComponentManager<Fact, CompositeFact> componentManager = new ComponentManager<>(this);
    private Long     id = null;
    private FactType type;



    protected  Fact (){}
    public     Fact (@NotNull FactType factType){
        this.type = factType;
    }




    @Id @GeneratedValue
    @Column(name = "id")
    public     Long getId ()         { return id; }
    protected  void setId (Long id)  { this.id = id; }



    @ManyToOne(fetch=FetchType.LAZY)
    @JoinTable(name="Fact_FactType")
    public FactType   getType ()               { return type; }
    protected  void   setType (FactType type)  { this.type = type; }



    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_fact")
    @Override
    public CompositeFact getParent ()                      { return componentManager.getParent(); }
    public void          setParent (CompositeFact parent)  { componentManager.setParent(parent); }




 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/





    @Override
    public String toString() {
        return this.getType().toString();
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + type.hashCode();
        return result;
    }




    // Old equals:
    //    @Override
    //    public boolean equals(Object obj) {
    //        if(!(obj instanceof Fact)) return false;
    //        Fact fact = (Fact) obj;
    //        if(this.id.equals(fact.id) && this.type.equals(fact.type))
    //            return true;
    //        else return false;
    //    }

    private static final EqualCheck defaultEqualCheck = EqualCheck.pk_if_exists_and_deeep;
    //private static final EqualCheck defaultEqualCheck = EqualCheck.pk_if_exists;

    @Override
    public boolean equals(Object obj) {
        return equals(obj, defaultEqualCheck);
    }

    public boolean equals(Object obj, EqualCheck equalCheck) {
        if(this==obj) return true;
        if(obj == null) return false;
        if ( !(obj instanceof Fact) ) return false;

        Fact fact = (Fact) obj;

        switch(equalCheck)
        {
            case pk_forced:              return equals_pkForcedCheck(fact);
            case pk_if_exists:           return equals_pkIfExistsCheck(fact);
            case deep:                   return equals_deepCheck(fact);
            case pk_if_exists_and_deeep: return equals_pkIfExistsAndDeepCheck(fact);
            case pk_forced_and_deeep:    return equals_pkForcedAndDeepCheck(fact);
            default: return false;
        }
    }

    public enum EqualCheck { pk_forced, pk_if_exists, deep, pk_if_exists_and_deeep, pk_forced_and_deeep}


     /* EQUALS
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
     * @param fact
     * @return
     */
    private boolean equals_pkForcedCheck(Fact fact) {
        if(this.id != null && fact.id != null)
            return this.id.equals(fact.id);
        else return false;
    }

    /**
     * Check the value of the properties, excluding the primary key value (id).
     * @param fact
     * @return
     */
    private boolean equals_deepCheck(Fact fact) {
        if(this.type == null)
            return fact.type == null; // true if both are null, false otherwise
        else return this.type.equals(fact.type);
    }

    /**
     * Check if both objects have an instance of the primary key (id). If they have, it compares the two pk values and return
     * the compare results. If they don't, it return the result of a deep check.
     * @param fact
     * @return
     */
    private boolean equals_pkIfExistsCheck(Fact fact) {
        if(this.id != null && fact.id != null)
            return this.id.equals(fact.id);
        else return equals_deepCheck(fact);
    }

    /**
     * Deep equals check and, if exists, pk check.
     * If pk does not exists, returns deep equals check.
     * If pk exists returns (deep equals check) && (pk equals check)
     * @param fact
     * @return
     */
    private boolean equals_pkIfExistsAndDeepCheck(Fact fact) {
        boolean pkCheck = true;
        if(this.id != null  &&  fact.id != null)
            pkCheck = this.id.equals(fact.id);
        return pkCheck && equals_deepCheck(fact);
    }

    private boolean equals_pkForcedAndDeepCheck(Fact fact) {
        return equals_pkForcedCheck(fact) && equals_deepCheck(fact);
    }





    public class IllegalValueException extends Exception {}


}







/*  Per fare persistenza su generics, vedere: http://stackoverflow.com/questions/28695081/how-to-embed-generic-field-using-hibernate
    Riporto qua:

    Hibernate cannot persist generic fields due to Type Erasure.
    However, I've managed to find a simple workaround:

    1) Add @Access(AccessType.FIELD) annotation to the class.
    2) Add @Transient annotation to field you want to persist.
    3) Create a specific getter and setter which uses this field.
    4) Add @Access(AccessType.PROPERTY) to the getter.
    5) Make type of the field embeddable by adding @Embeddable property to the class.

    In this way you will be able to have an embedded property of specific type.
    Here is a modified code:

        @Entity
        @Access(AccessType.FIELD)
        public class Element<T>
        {
           @Transient
           private T value;

           @Access(AccessType.PROPERTY)
           private SpecificValue getValue() {
               return (SpecificValue) value;
           }

           private void setValue(SpecificValue v) {
               this.value = (T) v;
           }
        }
        ...
        @Embeddable
        public class ValueType {
        ...
 */
