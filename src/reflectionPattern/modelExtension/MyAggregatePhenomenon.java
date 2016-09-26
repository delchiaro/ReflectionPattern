package reflectionPattern.modelExtension;

import reflectionPattern.model.knowledge.Phenomenon;

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
        this.phenomenon = phen;
        this.code = code;
        this.description = description;
    }


    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="phen_id")
    public Phenomenon getPhenomenon() {
        return phenomenon;
    }
    protected void setPhenomenon(Phenomenon phen) {
        this.phenomenon = phen;
    }




    public String getCode() { return code; }
    public String getDescription() { return description; }
}
