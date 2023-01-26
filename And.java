package rigitz;

import java.util.List;

public class And extends Operations{
    private static final TreeType type = TreeType.union;
    private static final String opString = Tree.unionStr;
    private And(List<Tree> trees){
        this.addChild(trees);
    }

    public static Tree create(Tree... trees){
        if(!filter(trees, TreeType.empty).isEmpty())
            return Literal.empty();
        List<Tree> fTrees = filter(trees, TreeType.empty);

        if (fTrees.isEmpty())
            return Literal.empty();
        if(fTrees.size() == 1)
            return trees[0];
        Tree union = new And(filter(fTrees, TreeType.and));
        List<Tree> unions = filter(trees, inverse(TreeType.and));
        for (int i = 0; i < unions.size(); i++) {
            union.addChild(unions.get(i).getChildren());
        }
        return union;
    }

    @Override
    public Tree addChild(Tree... trees) {
        if(!filter(trees, TreeType.empty).isEmpty())
            return Literal.empty();
        List<Tree> fTrees = filter(trees, TreeType.and);
        for (Tree t : fTrees){
            children.add(t);
        }
        List<Tree> ands = filter(trees, inverse(TreeType.and));
        for (int i = 0; i < ands.size(); i++) {
            this.addChild(ands.get(i).getChildren());
        }
        return this;
    }
}
