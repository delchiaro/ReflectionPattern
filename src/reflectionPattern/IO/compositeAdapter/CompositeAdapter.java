package reflection.IO.compositeAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nagash on 06/09/16.
 */
public abstract class CompositeAdapter<ADAPTED, T> {

    private ADAPTED adapted;
    public CompositeAdapter(ADAPTED adaptedObject) {
        this.adapted = adaptedObject;
    }


    protected ADAPTED getAdapted(){
        return adapted;
    }

    //return null if adapted is a leaf.
    protected abstract Set<ADAPTED> getChilds();


    protected abstract CompositeAdapter newCompositeAdapter(ADAPTED adaptedObject);
    public abstract T getValue();


    //return null if adapted is a leaf.
    public Set<CompositeAdapter<ADAPTED,T>> getAdaptedChilds() {
        if(getChilds() == null)
            return null;

        Set<CompositeAdapter<ADAPTED,T>>  adaptedSet = new HashSet<CompositeAdapter<ADAPTED,T>>();

        for( ADAPTED child : getChilds() )
        {
            adaptedSet.add(newCompositeAdapter(child));
        }
        return adaptedSet;
    }
}