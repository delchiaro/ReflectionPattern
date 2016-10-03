package reflectionPattern.model.knowledge;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;

/**
 * Created by nagash on 22/09/16.
 */

@Entity
@Access(AccessType.PROPERTY)
public class MyAggregatePhenomenon {
    private Long id;
    private String code;
    private String description;
    private Phenomenon phenomenon;



    protected MyAggregatePhenomenon() {
        this.phenomenon = null;
    }
    public MyAggregatePhenomenon(Phenomenon phen, String code, String description) {
        this.code = code;
        this.description = description;
        assignPhenomenon(phen);
    }
    public MyAggregatePhenomenon(MyAggregatePhenomenon copy) {
        this.code = copy.code;
        this.description = copy.description;
        this.phenomenon = copy.phenomenon;
    }

    public MyAggregatePhenomenon deepCopy() {
        return new MyAggregatePhenomenon(this);
    }


    @Id  @GeneratedValue @Column(name="id")
    public Long getId() {
        return id;
    }
    protected void setId(Long id) {
        this.id = id;
    }


    @Column
    public String getCode() { return code; }
    public void setCode(String code) {
        this.code = code;
    }


    @Column
    public String getDescription() { return description; }
    protected void setDescription(String description) {
        this.description = description;
    }



    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name="id_phen")
    @LazyToOne(LazyToOneOption.NO_PROXY)
    public Phenomenon getPhenomenon() {
        return phenomenon;
    }
    protected void setPhenomenon(Phenomenon phen) {
        this.phenomenon = phen;
    }
    public void assignPhenomenon(Phenomenon phen) {
        setPhenomenon(phen);
        phen.setAggregatePhenomenon(this);
    }


    @Override
    public String toString() {
        return " - " + code; // + " - " + description;
        //return "";
    }

}
