package reflectionPattern.model.knowledge;

import javax.persistence.*;

/**
 * Created by nagash on 22/09/16.
 */

@Entity
@Access(AccessType.FIELD)
public class MyAggregatePhenomenon {


    @Id  @GeneratedValue @Column(name="id")  private Long id;
    @Transient private Phenomenon phenomenon;

    private String code;
    private String description;



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



    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="id")
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



    public String getCode() { return code; }
    public String getDescription() { return description; }


}
