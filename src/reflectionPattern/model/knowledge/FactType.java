/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;

import javax.persistence.*;

@Entity

//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "FACT_TYPE_DISCRIM", discriminatorType = DiscriminatorType.STRING )
public abstract class FactType {
    //private CompositeType   fatherType = null; // serve avere la bidirezionalitá?
//    void setFatherType(CompositeType fatherType){
//        this.fatherType = fatherType;
//    }



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
    public FactType(String typeName){
        this.typeName = typeName;
    }


    @Column(name = "NAME")
    public String getTypeName(){
        return typeName;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FactType)) return false;
        FactType fType = (FactType) obj;
        if(fType.getId().equals(this.getId()) && fType.getTypeName().equals(this.getTypeName()) )
            return true;
        else return false;
    }

    @Override
    public String toString() {
        return this.getTypeName();
    }
}
