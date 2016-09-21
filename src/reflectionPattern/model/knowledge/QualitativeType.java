/**
 * Created by nagash on 31/08/16.
 */
package reflectionPattern.model.knowledge;


import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@DiscriminatorValue("QUALITATIVE")
public class QualitativeType extends FactType
{

    Set<Phenomenon> legalPhenomenons = new HashSet<>();


    protected  QualitativeType () {}
    public     QualitativeType (@NotNull  String typeName) { super(typeName); }



    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public    Set<Phenomenon>  getLegalPhenomenons ()                        { return Collections.unmodifiableSet(legalPhenomenons); }
    protected void             setLegalPhenomenons (Set<Phenomenon> phenoms) { this.legalPhenomenons = phenoms; }






 /* *******************************************************************************************************************
    *******************************************************************************************************************
    *******************************************************************************************************************/



    public void addLegalPhenomenon(@NotNull Phenomenon newLegalPhenomenon) {
        legalPhenomenons.add(newLegalPhenomenon);
    }
    public boolean isPhenomenonLegal(Phenomenon testPhenomenon) {
        if(testPhenomenon == null) return false;
        if(legalPhenomenons.contains(testPhenomenon))
            return true;
        else return false;
    }



    @Override public int hashCode() {
        int result = super.hashCode();
        //result = 31 * result + (legalPhenomenons != null ? legalPhenomenons.hashCode() : 0);
        return result;
    }

    @Override public boolean equals(Object obj) {
        if(this==obj) return true;
        if(super.equals(obj) == false) return false;
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
