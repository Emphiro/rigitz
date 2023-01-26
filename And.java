package rigitz;

import java.util.List;

public class And extends Operations{
    private static final TreeType type = TreeType.union;
    private static final String opString = Tree.andStr;
    private And(List<Tree> trees){
        this.addChild(trees);
    }

    public static Tree create(Tree... trees){
        if(!filter(trees, inverse(TreeType.empty)).isEmpty())
            return Literal.empty();

        if (trees.length == 0)
            return Literal.empty();
        if(trees.length == 1)
            return trees[0];
        Tree and = new And(filter(trees, TreeType.and));
        List<Tree> ands = filter(trees, inverse(TreeType.and));
        for (int i = 0; i < ands.size(); i++) {
            and.addChild(ands.get(i).getChildren());
        }
        return and;
    }

    @Override
    public Tree addChild(Tree... trees) {
        if(!filter(trees, inverse(TreeType.empty)).isEmpty())
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

    @Override
    public TreeType getType(){
        return TreeType.and;
    }

    @Override
    public String toString() {
        return super.toString(opString);
    }
}
