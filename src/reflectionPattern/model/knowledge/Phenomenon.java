package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;

/**
 * Created by nagash on 02/09/16.
 */
@Access(AccessType.FIELD)
@Inheritance( strategy = InheritanceType.JOINED )
@Entity
public class Phenomenon {


    @Transient
    private MyAggregatePhenomenon aggregatePhenomenon = null;

    protected Phenomenon() {}
    public Phenomenon(@NotNull String value){
        this.value = value;
    }
    public Phenomenon(@NotNull Phenomenon copy)
    {
        this.value = copy.value;
        if(copy.aggregatePhenomenon != null)
        {
            this.aggregatePhenomenon = copy.aggregatePhenomenon.deepCopy();
            aggregatePhenomenon.setPhenomenon(this);
        }
    }


    public Phenomenon deepCopy() {
        return new Phenomenon(this);
    }

    @Column(name = "id")
    @Id @GeneratedValue
    private Long id = null;



    @Column
    private String value;
    public String getValue() {
        return value;
    }




    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="id")
    @LazyToOne(LazyToOneOption.NO_PROXY)
    public MyAggregatePhenomenon getAggregatePhenomenon() {
        return aggregatePhenomenon;
    }
    protected void setAggregatePhenomenon(MyAggregatePhenomenon aggregatePhenomenon) {
        this.aggregatePhenomenon = aggregatePhenomenon;
    }
//    protected void onAggregatePhenAssignPhen(MyAggregatePhenomenon aggregatePhenomenon){
//        this.aggregatePhenomenon = aggregatePhenomenon;
//    }




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
