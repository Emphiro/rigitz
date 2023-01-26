package rigitz;

import java.util.List;

public class Union extends Operations {
    private static final String opString = Tree.unionStr;
    private Union(List<Tree> trees){
        this.addChild(trees);
    }

    public static Tree create(Tree... trees){
        List<Tree> fTrees = filter(trees, TreeType.empty);
        if (fTrees.isEmpty())
            return Literal.empty();
        if(fTrees.size() == 1)
            return fTrees.get(0);
        Tree union = new Union(filter(fTrees, TreeType.union));
        List<Tree> unions = filter(trees, inverse(TreeType.union));
        for (int i = 0; i < unions.size(); i++) {
            union.addChild(unions.get(i).getChildren());
        }
        return union;
    }

    public static Tree create(List<Tree> trees){
        return create(trees.toArray(new Tree[0]));
    }

    @Override
    public Tree addChild(Tree... trees) {
        List<Tree> fTrees = filter(trees, TreeType.empty, TreeType.union);
        for (Tree t : fTrees){
            children.add(t);
        }
        List<Tree> unions = filter(trees, inverse(TreeType.union));
        for (int i = 0; i < unions.size(); i++) {
            this.addChild(unions.get(i).getChildren());
        }
        return this;
    }

    @Override
    public TreeType getType(){
        return TreeType.union;
    }

    @Override
    public String toString() {
        return super.toString(opString);
    }
}
