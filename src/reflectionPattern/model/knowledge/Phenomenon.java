package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;

/**
 * Created by nagash on 02/09/16.
 */
@Access(AccessType.FIELD)
@Inheritance( strategy = InheritanceType.JOINED )
@Entity
public class Phenomenon {



    protected Phenomenon() {}
    public Phenomenon(@NotNull String value){
        this.value = value;
    }




    @Column(name = "id")
    @Id @GeneratedValue
    private Long id = null;



    @Column
    private String value;
    public String getValue() {
        return value;
    }





 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/



    @Override public String toString() {
        return value;
    }

    @Override public boolean equals(Object obj) {
        if(this==obj) return true;
        //if(super.equals(obj) == false) return false;
        if(obj == null) return false;
        if(! (obj instanceof Phenomenon)) return false;

        Phenomenon phen = (Phenomenon) obj;


        // TODO: what to do with ID etc... ? For now I'm using "pk_if_exists_and_deeep" strategy
        boolean id_equals = true;
        if(phen.id != null && this.id != null)
            id_equals = this.id.equals(phen.id);

        if(this.value == null)
            return id_equals && phen.value == null;
        else return id_equals && this.value.equals(phen.value);

    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + value.hashCode();
        return result;
    }

}
