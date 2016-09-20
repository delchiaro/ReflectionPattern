package reflectionPattern.utility.compositeWithAncestors;

import com.sun.istack.internal.NotNull;

import java.util.*;

/**
 * Created by nagash on 17/09/16.
 */

public class CompositeManagerALS  <CONTAINER extends ICompositeALS<COMPONENT>,    COMPONENT extends IComponentALS>
        extends ComponentALS
        implements ICompositeALS<COMPONENT>

{
    private CONTAINER container;
    public CONTAINER getContainer(){
        return container;
    }

    private Set<COMPONENT> childs = new HashSet<>();


    @Override public void addChild(COMPONENT child) {

        this.childs.add(child);
        //childType.setFatherType(this);
        pushAncestorsToNewChild(child);
    }


    @Override public Set<COMPONENT> getChilds() {
        return Collections.unmodifiableSet(childs);
    }


    public void setChilds(Set<COMPONENT> childs) {
        this.childs = childs;
    }


    private void pushAncestorsToNewChild(@NotNull COMPONENT child)
    {
        // When new Child is added to this compositeWithAncestors, I add all my ancestors and myself to the Child ancestors.

        // The FIRST ELEMENT OF THE LIST of is the direct ancestor of mySelf (my FATHER).
        // The LAST ELEMENT OF THE LIST is the most far ancestor of mySelf (my family founder, ROOT COMPOSITE).

        Iterator<CompositeManagerALS> iter = getAncestors().iterator();
        while(iter.hasNext())
            child.addFirstAncestor(iter.next()); // add at the beginning of the list
        child.addFirstAncestor(this);


        // If the new child is a CompositeType, there could be a problem:
        // the compositeChild could have already added some child, so I have to tell to the child of the compositeChild
        // that I am the father of his father (grandfather), and I have to tell them about all my ancestors.

        // NB: a child can have only 1 father, so the child of the compChild didn't know anything about their grandfather
        // because if I'm adding now the compChild to a compositeWithAncestors, it means that compChild never had a father until now,
        // and so the childs of the compChild never had idea of what was their's grandfather and other ancestors were.
        // They only know who was their father...
        // So I have simply to add to their ancestors all the ancestors of the father, which until now were unknown.
        if(child instanceof CompositeManagerALS)
        {
            CompositeManagerALS compChild = (CompositeManagerALS) child;
            if(compChild.getChilds().size() > 0) // (redundant check)
                compChild.updateChildsAncestors(compChild.getAncestors());
            // if the new child added is a compositeWithAncestors, and if he has childs, I update these child about their ancestors
            // (from grandfather to the root: this == grandfather of the childs of compChild)
        }
    }

    private void updateChildsAncestors(List<CompositeManagerALS> newAncestors)
    {
        for(COMPONENT child : childs)
        {
            // Add the new anchestor at the end of the child's ancestors list:
            child.appendAllAncestors( newAncestors );

            if(child instanceof CompositeManagerALS)
                ((CompositeManagerALS) child).updateChildsAncestors(newAncestors);
        }
    }



}
