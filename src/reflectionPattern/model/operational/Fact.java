/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.operational;

import reflectionPattern.model.knowledge.FactType;

import javax.persistence.*;



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

@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "FACT_DISCRIM", discriminatorType = DiscriminatorType.STRING )
public abstract class Fact {

    @Id @GeneratedValue
    @Column(name = "id")
    private Long id = null;
    public Long getId() {
        return id;
    }

    @ManyToOne
    private FactType type;


    protected Fact(){}
    public Fact(FactType factType){
        this.type = factType;
    }
    public FactType getType() {
        return type;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Fact)) return false;
        Fact fact = (Fact) obj;
        if(this.id.equals(fact.id) && this.type.equals(fact.type))
            return true;
        else return false;
    }


    public class IllegalValueException extends Exception {}



    @Override
    public String toString() {
        return this.getType().toString();
    }
}

