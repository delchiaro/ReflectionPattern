//package utility.compositeWithAncestors.out;
//
//
//import utility.compositeWithAncestors.IComponentALS;
//import utility.compositeWithAncestors.ICompositeALS;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Set;
//
///**
// * Created by nagash on 18/09/16.
// */
//public class CompositeTree {
//
//
//
//    public static String getTree(IComponentALS head)
//    {
//        ArrayList<Boolean> lastInLevel = new ArrayList<>();
//        lastInLevel.add(true);
//
//        return compositeExplorer(head, 0, lastInLevel);
//    }
//
//    private static String compositeExplorer(IComponentALS node, int level, ArrayList<Boolean> lastInLevel )
//    {
//        String ret;
//        if(node instanceof ICompositeALS == false)
//            ret = punteggiatura(level, NodeType.LEAF, lastInLevel) + node.toString();
//        else
//        {
//            ICompositeALS compNode = (ICompositeALS) node;
//
//            if (compNode.getChilds() == null || compNode.getChilds().size() == 0)
//                ret = punteggiatura(level, NodeType.LEAF, lastInLevel) + node.toString();
//
//            else
//            {
//                ret = punteggiatura(level, NodeType.COMPOSITE, lastInLevel) + node.toString();
//
//                int newlevel = level + 1;
//                int nChilds = compNode.getChilds().size();
//
//
//                Set<IComponentALS> childs = compNode.getChilds();
//                Iterator<IComponentALS> iter = childs.iterator();
//
//                lastInLevel.add(false);
//                for (int i = 0; i < nChilds - 1; i++)
//                    ret += compositeExplorer(iter.next(), newlevel, lastInLevel);
//
//                lastInLevel.set(newlevel, true);
//                ret += compositeExplorer(iter.next(), newlevel, lastInLevel);
//                lastInLevel.remove(newlevel);
//                ret += punteggiatura(newlevel, NodeType.EMPTY_LINE, lastInLevel);
//            }
//
//        }
//        return ret;
//
//    }
//
//
//    public static void printTree(IComponentALS root) {
//        System.out.print(getTree(root));
//    }
//
//    public enum NodeType { LEAF,COMPOSITE,EMPTY_LINE; }
//
//
//
//
//
//
//    private static String punteggiatura (int level, NodeType nodeType, ArrayList<Boolean> lastInLevel)
//    {
//
//        String str = "\n";
//        for(int i = 0; i<level; i++)
//        {
//            if( lastInLevel.get(i) == false)
//                str += "│ ";
//            else
//                str += "  ";
//        }
//
//        switch(nodeType)
//        {
//            case EMPTY_LINE:
//                return str +  " ";
//            case LEAF:
//                if(level == 0)
//                    return str +  "-";
//                else if(lastInLevel.get(level))
//                    return str +  "└ ";
//                else
//                    return str +  "├ ";
//            case COMPOSITE:
//                if(level == 0)
//                    return str +  "-";
//                else if(lastInLevel.get(level))
//                    return str +  "└ ";
//                else
//                    return str +  "├ ";
//        }
//
//
//        return null;
//    }
//}
