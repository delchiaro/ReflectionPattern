package reflectionPattern.modelExtension;

import reflectionPattern.model.knowledge.Phenomenon;

import javax.persistence.Entity;

/**
 * Created by nagash on 22/09/16.
 */
@Entity
public class MySubPhenomeon extends Phenomenon {

    String code;
    String description;

    protected MySubPhenomeon() {}

    public MySubPhenomeon( String code, String description) {
        super(description);
        this.code = code;
        this.description = description;
    }




    public String getCode() { return code; }
    public String getDescription() { return description; }


}
