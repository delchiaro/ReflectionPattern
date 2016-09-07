/**
 * Created by nagash on 31/08/16.
 */
package reflection.model.knowledge;


import reflection.model.knowledge.quantity.Unit;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("QUALITATIVE")
public class QualitativeType extends FactType {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Phenomenon> legalPhenomenons = new HashSet<>();

    protected QualitativeType() {}
    public QualitativeType(String typeName) {
        super(typeName);
    }

    public void addLegalPhenomenon(Phenomenon newLegalPhenomenon) {
        legalPhenomenons.add(newLegalPhenomenon);
    }
    public boolean isPhenomenonLegal(Phenomenon testPhenomenon) {
        if(legalPhenomenons.contains(testPhenomenon))
            return true;
        else return false;
    }
    public Set<Phenomenon> getLegalPhenomenons() {
        return Collections.unmodifiableSet(legalPhenomenons);
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof  QualitativeType)) return false;
        QualitativeType qlObj = (QualitativeType)obj;
        if( super.equals(qlObj) &&  qlObj.getLegalPhenomenons().size() == this.getLegalPhenomenons().size()  )
        {
            for (Phenomenon phen : qlObj.getLegalPhenomenons())
            {
                if( ! this.getLegalPhenomenons().contains(phen) ) // Set<Phenomenon>.contains(phen) usa  phen.equals() (equivalenza) non operatore == (identita')
                    return false;
            }
            return true;
        }
        else return false;
    }


}
