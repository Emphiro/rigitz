package rigitz;

import java.util.ArrayList;
import java.util.List;

public abstract class MultipleOps extends Tree{
    private static final TreeType type = TreeType.union;
    private List<Tree> children = new ArrayList<>();

    private Union(List<Tree> trees){
        this.addChild(trees);
    }

    public static Tree createUnion(Tree ... trees){
        List<Tree> fTrees = filter(trees, TreeType.empty);

        if (fTrees.isEmpty())
            return Literal.empty();
        Union union = new Union(filter(fTrees, TreeType.union));
        List<Tree> unions = filter(trees, inverse(TreeType.union));
        for (int i = 0; i < unions.size(); i++) {
            union.addChild(unions.get(i).getChildren());
        }
        return union;
    }

    @Override
    public List<Tree> getChildren() {
        return children;
    }

    @Override
    public int getNumChildren() {
        return children.size();
    }



    @Override
    public void addChild(Tree... trees) {
        List<Tree> fTrees = filter(trees, TreeType.empty, TreeType.union);
        for (Tree t : fTrees){
            children.add(t);
        }
        List<Tree> unions = filter(trees, inverse(TreeType.union));
        for (int i = 0; i < unions.size(); i++) {
            this.addChild(unions.get(i).getChildren());
        }
    }

    @Override
    public String toString() {
        if(isEmpty())
            return Tree.emptyStr;
        StringBuilder sb = new StringBuilder("(");
        sb.append(children.get(0).toString());
        for(int i = 1; i < getNumChildren(); i++) {
            sb.append(Tree.unionStr).append(children.get(i).toString());
        }
        sb.append(")");
        return sb.toString();
    }
}
