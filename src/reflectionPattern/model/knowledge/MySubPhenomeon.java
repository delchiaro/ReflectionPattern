package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.Phenomenon;

import javax.persistence.*;

/**
 * Created by nagash on 22/09/16.
 */
@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("MY_SUB_PHEN")
public class MySubPhenomeon extends Phenomenon {

    String code;
    String description;

    protected MySubPhenomeon() {}
    public MySubPhenomeon( String code, String description) {
        super(description);
        this.code = code;
        this.description = description;
    }
    public MySubPhenomeon(@NotNull MySubPhenomeon copy) {
        super(copy);
        this.code = copy.code;
        this.description = copy.description;
    }

    @Override
    public MySubPhenomeon deepCopy() {
        return new MySubPhenomeon(this);
    }

    @Column
    public String getCode() { return code; }
    protected void setCode(String code) {
        this.code = code;
    }

    @Column
    public String getDescription() { return description; }
    public void setDescription(String descr) {
        this.description = descr;
    }


    @Override
    public String toString() {
        return super.toString() + " - " + code;
    }
}
