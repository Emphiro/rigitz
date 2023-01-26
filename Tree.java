package rigitz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Tree {
    static final String unionStr = " U ";
    static final String andStr = "";
    static final String starStr = "*";
    static final String epsilonStr = "e";
    static final String emptyStr = "";
    static final String indentation = "  ";
    private static TreeType type;

    public abstract List<Tree> getChildren();

    public abstract int getNumChildren();
    public Tree addChild(List<Tree> tree){
        return addChild(tree.toArray(new Tree[0]));
    }
    public abstract Tree addChild(Tree... tree);
    public abstract String toString();

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() != Tree.class)
            return false;
        Tree tree = (Tree) obj;
        if(tree.getType() != this.getType())
            return false;
        if(tree.getType() == TreeType.literal)
            return tree.toString().equals(this.toString());

        if(tree.getNumChildren() != this.getNumChildren())
            return false;

        for (int i = 0; i < tree.getNumChildren(); i++) {
            if(equals(tree.getChildren().get(i), this.getChildren().get(i)))
                return false;
        }
        return true;
    }
    public static boolean equals(Tree t1, Tree t2){
        return t1.equals(t2);
    }

    public boolean isEmpty(){
        return getNumChildren() == 0 && getType() != TreeType.literal;
    }

    public TreeType getType(){
        return type;
    }
    public static List<Tree> filter(Tree[] unfiltered, TreeType... filter){
        List<Tree> filtered = new ArrayList<>();
        for (Tree tree : unfiltered){
            if(tree.getType() != type)
                filtered.add(tree);
        }
        return filtered;
    }
    public static List<Tree> filter(List<Tree> unfiltered, TreeType... filter){
        return filter(unfiltered.toArray(new Tree[0]), filter);
    }

    public static TreeType[] inverse(TreeType... type){
        return inverse(Arrays.asList(type));
    }

    public static TreeType[] inverse(List<TreeType> type){
        ArrayList<TreeType> result = new ArrayList<>();
        for (TreeType t : TreeType.values() ){
            if(!type.contains(t))
                result.add(t);
        }
        return result.toArray(new TreeType[0]);
    }
    public static Tree empty(){
        return Literal.empty();
    }
    public static Tree epsilon(){
        return Literal.epsilon();
    }


}
