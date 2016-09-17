/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.operational.CompositeFact;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity

//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "FACT_TYPE_DISCRIM", discriminatorType = DiscriminatorType.STRING )
public abstract class FactType {
    //private CompositeType   fatherType = null; // serve avere la bidirezionalitá?
//    void setFatherType(CompositeType fatherType){
//        this.fatherType = fatherType;
//    }

    // ANCESTOR StRATEGY * * * * * * * * * * * * * * * * * * * *
    @OneToMany(fetch=FetchType.LAZY )
    private List<CompositeType> ancestors = new LinkedList<>();

    public void addAncestor(CompositeType ancestorType) {
        ancestors.add(ancestorType);
    }
    public List<CompositeType> getAncestors() {
        return Collections.unmodifiableList(ancestors);
    }
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *




    // In this way it doesn't works with the current FactTypeDAO: in java whe have parent_type always null :(
    // it would be cool to have a bidirectional java link.. but I think we would make a JOIN in the "findAllRoots" query method.
    //    @ManyToOne
    //    private FactType parent_type;

    @Column(name="parent_type")
    private Long parent_type;
    // this property (bidirectional access) is needed from the FactTypeDAO.. this field would be still created in the relactional model
    // but JPA need the java property, otherwise the field can't be accessed from a SELECT query in JPQL (sure..?)



    @Id @GeneratedValue
    @Column(name = "id")
    private Long id = null;
    public Long getId() {
        return id;
    }

    private String typeName;


    protected FactType() {
        this.typeName=null;
    }
    public FactType(@NotNull  String typeName){
        this.typeName = typeName;
    }


    @Column(name = "NAME")
    public String getTypeName(){
        return typeName;
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

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + typeName.hashCode();
        // TODO: include parentType in the hashcode? I don't think it's good.. that properties is there only for hibernate mapping (for the DAO.. see comments)
        return result;
    }

    @Override
    public String toString() {
        return this.getTypeName();
    }
}
