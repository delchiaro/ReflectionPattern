package reflection.model.knowledge;

import javax.persistence.*;

/**
 * Created by nagash on 02/09/16.
 */
@Entity
public class Phenomenon {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id = null;

    @Column
    private String value;

    protected Phenomenon() {}
    public Phenomenon(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof  Phenomenon)) return false;
        if(((Phenomenon)obj).id.equals(this.id) && ((Phenomenon)obj).value.equals(this.value))
            return true;
        else return false;
    }

    @Override
    public String toString() {
        return value; //+ "  -  JAVA_ADDRESS:" + super.toString();
    }
}
