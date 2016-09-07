/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.knowledge;

import reflection.model.operational.QualitativeFact;

import javax.persistence.*;

@Entity

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "FACT_TYPE_DISCRIM", discriminatorType = DiscriminatorType.STRING )
public abstract class FactType {
    //private CompositeType   fatherType = null; // serve avere la bidirezionalitá?
//    void setFatherType(CompositeType fatherType){
//        this.fatherType = fatherType;
//    }

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
}
