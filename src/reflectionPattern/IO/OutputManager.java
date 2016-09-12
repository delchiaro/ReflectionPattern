package reflectionPattern.IO;

import org.hibernate.Hibernate;
import reflectionPattern.IO.compositeAdapter.CompositeAdapter;
import reflectionPattern.IO.compositeAdapter.FactCompositeAdapter;
import reflectionPattern.IO.compositeAdapter.FactTypeCompositeAdapter;
import reflectionPattern.model.knowledge.FactType;
import reflectionPattern.model.operational.Fact;

import java.util.ArrayList;

/**
 * Created by nagash on 04/09/16.
 */
public class OutputManager {



//    public static String knowledgeExplorer(FactType head)
//    {
//        ArrayList<Boolean> lastInLevel = new ArrayList<>();
//        lastInLevel.add(true);
//
//        return knowledgeExplorer(head, 0, lastInLevel);
//    }
//
//    private static String knowledgeExplorer(FactType node, int level, ArrayList<Boolean> lastInLevel )
//    {
//        String ret;
//        if(node.getClass() != CompositeType.class)
//            ret = punteggiatura(level, NodeType.LEAF, lastInLevel) + node.getTypeName();
//
//        else
//        {
//            CompositeType compNode = (CompositeType)node;
//            if(compNode.getChildTypes().size() == 0)
//            {
//                ret = punteggiatura(level, NodeType.LEAF, lastInLevel) + node.getTypeName();
//            }
//            else
//            {
//                ret = punteggiatura(level, NodeType.COMPOSITE, lastInLevel) + node.getTypeName();
//
//                int newlevel = level+1;
//                int nChilds = compNode.getChildTypes().size();
//                FactType[] childs = new FactType[nChilds];
//                childs = compNode.getChildTypes().toArray(childs);
//                lastInLevel.add(false);
//                for (int i=0 ; i<nChilds-1 ; i++)
//                {
//                    ret += knowledgeExplorer(childs[i], newlevel, lastInLevel);
//                }
//                lastInLevel.set(newlevel, true);
//                ret += knowledgeExplorer(childs[nChilds-1], newlevel, lastInLevel);
//                lastInLevel.remove(newlevel);
//                ret += punteggiatura (newlevel, NodeType.EMPTY_LINE, lastInLevel);
//
//            }
//        }
//
//        return ret;
//
//    }




    public static String adapterExplorer(CompositeAdapter head)
    {
        ArrayList<Boolean> lastInLevel = new ArrayList<>();
        lastInLevel.add(true);

        return adapterExplorer(head, 0, lastInLevel);
    }

    private static String adapterExplorer(CompositeAdapter node, int level, ArrayList<Boolean> lastInLevel )
    {
        String ret;
        if(node.getAdaptedChilds() == null || node.getAdaptedChilds().size() == 0 )
            ret = punteggiatura(level, NodeType.LEAF, lastInLevel) + node.getValue();

        else
        {
            ret = punteggiatura(level, NodeType.COMPOSITE, lastInLevel) + node.getValue();

            int newlevel = level+1;
            int nChilds = node.getAdaptedChilds().size();

            CompositeAdapter[] childs = new CompositeAdapter[nChilds];
            childs = (CompositeAdapter[]) node.getAdaptedChilds().toArray(childs);

            lastInLevel.add(false);
            for (int i=0 ; i<nChilds-1 ; i++)
            {
                ret += adapterExplorer(childs[i], newlevel, lastInLevel);
            }
            lastInLevel.set(newlevel, true);
            ret += adapterExplorer(childs[nChilds-1], newlevel, lastInLevel);
            lastInLevel.remove(newlevel);
            ret += punteggiatura (newlevel, NodeType.EMPTY_LINE, lastInLevel);

        }


        return ret;

    }


    public static void printFactTypeTree(FactType root) {
        System.out.print(adapterExplorer(new FactTypeCompositeAdapter(root)));
    }

    public static void printFactTree(Fact root) {
        System.out.print(adapterExplorer(new FactCompositeAdapter(root)));
    }



    public enum NodeType { LEAF,COMPOSITE,EMPTY_LINE; }






    private static String punteggiatura (int level, NodeType nodeType, ArrayList<Boolean> lastInLevel)
    {

        String str = "\n";
        for(int i = 0; i<level; i++)
        {
            if( lastInLevel.get(i) == false)
                str += "│ ";
            else
                str += "  ";
        }

        switch(nodeType)
        {
            case EMPTY_LINE:
                return str +  " ";
            case LEAF:
                if(level == 0)
                    return str +  "-";
                else if(lastInLevel.get(level))
                    return str +  "└ ";
                else
                    return str +  "├ ";
            case COMPOSITE:
                if(level == 0)
                    return str +  "-";
                else if(lastInLevel.get(level))
                    return str +  "└ ";
                else
                    return str +  "├ ";
        }


        return null;
    }

}
