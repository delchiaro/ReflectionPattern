package reflectionPattern.model.knowledge;

import com.sun.istack.internal.NotNull;
import reflectionPattern.model.knowledge.Phenomenon;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by nagash on 22/09/16.
 */
@Entity
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
    public Phenomenon deepCopy() {
        return new MySubPhenomeon(this);
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }


}
