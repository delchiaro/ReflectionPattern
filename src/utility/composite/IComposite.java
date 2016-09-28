package utility.composite;


import java.util.Set;

/**
 * Created by nagash on 18/09/16.
 */
public interface IComposite<COMPOSITE extends IComposite, COMPONENT extends IComponent> extends IComponent<COMPOSITE>  {


    public void addChild(COMPONENT child);
    public Set<COMPONENT> getChilds();
    //public void setChilds(Set<COMPONENT> childs);



}
