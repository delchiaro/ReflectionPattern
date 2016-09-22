package reflectionPattern.utility.compositeWithAncestors;

import com.sun.istack.internal.NotNull;

import java.util.*;

/**
 * Created by nagash on 17/09/16.
 */

public class CompositeManagerALS  <CONTAINER extends ICompositeALS<CONTAINER, COMPONENT>,    COMPONENT extends IComponentALS<CONTAINER>>
{
    private CONTAINER container;
    public CONTAINER getContainer(){
        return container;
    }
    private Set<COMPONENT> childs = new HashSet<>();

    protected CompositeManagerALS() { }
    public CompositeManagerALS(CONTAINER container) {
        this.container = container;
    }


    public void addChild(COMPONENT child) {

        this.childs.add(child);
        //child.setParent(this.getContainer()); // set parent is FAKE, don't  use it. Use updatable = false in hibernate associations
        pushAncestorsToNewChild(child);
    }


    public Set<COMPONENT> getChilds() {
        return (childs);
    }


    public void setChilds(Set<COMPONENT> childs) {
        this.childs = childs;
    }


    private void pushAncestorsToNewChild(@NotNull COMPONENT child)
    {
        // When new Child is added to this compositeWithAncestors, I add all my ancestors and myself to the Child ancestors.

        // The FIRST ELEMENT OF THE LIST of is the direct ancestor of mySelf (my FATHER).
        // The LAST ELEMENT OF THE LIST is the most far ancestor of mySelf (my family founder, ROOT COMPOSITE).

        Iterator<CONTAINER> iter = getContainer().getAncestors().iterator();
        while(iter.hasNext())
            child.addFirstAncestor(iter.next()); // add at the beginning of the list
        child.addFirstAncestor(this.getContainer());


        // If the new child is a CompositeType, there could be a problem:
        // the compositeChild could have already added some child, so I have to tell to the child of the compositeChild
        // that I am the father of his father (grandfather), and I have to tell them about all my ancestors.

        // NB: a child can have only 1 father, so the child of the compChild didn't know anything about their grandfather
        // because if I'm adding now the compChild to a compositeWithAncestors, it means that compChild never had a father until now,
        // and so the childs of the compChild never had idea of what was their's grandfather and other ancestors were.
        // They only know who was their father...
        // So I have simply to add to their ancestors all the ancestors of the father, which until now were unknown.
        if(child instanceof ICompositeALS)
        {
            ICompositeALS compositeChild = (ICompositeALS) child;
            if(compositeChild.getChilds().size() > 0) // (redundant check)
                updateChildsAncestors(compositeChild, compositeChild.getAncestors());
            // if the new child added is a compositeWithAncestors, and if he has childs, I update these child about their ancestors
            // (from grandfather to the root: this == grandfather of the childs of compChild)
        }
    }

    private static <CONTAINER extends ICompositeALS, COMPONENT extends IComponentALS> void
                updateChildsAncestors (ICompositeALS<CONTAINER, COMPONENT> childsFather, List<CONTAINER> newAncestors)
    {
        for(COMPONENT child : childsFather.getChilds())
        {
            // Add the new anchestor at the end of the child's ancestors list:
            child.appendAllAncestors( newAncestors );

            if(child instanceof ICompositeALS)
                updateChildsAncestors((ICompositeALS) child, newAncestors);
        }
    }


    public List<? extends IComponentALS> offsprings() {
        return offsprings(this.getContainer());
    }

    private static List<? extends IComponentALS> offsprings(ICompositeALS<?,?> head)
    {
        List<IComponentALS> list = new LinkedList<>();

        for (IComponentALS child : head.getChilds())
        {
            if(child instanceof ICompositeALS)
                list.addAll( offsprings( (ICompositeALS<?,?>)child ) );
            else
                list.add(child);
        }
        return list;
    }



}
