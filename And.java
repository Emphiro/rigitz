package rigitz;

import java.util.List;

public class And extends Operations{
    private static final TreeType type = TreeType.union;
    private static final String opString = Tree.andStr;
    private And(List<Tree> trees){
        this.addChild(trees);
    }

    private And() { }

    public static Tree create(List<Tree> trees){
        return create(trees.toArray(new Tree[0]));
    }
    public static Tree create(Tree... trees){
        if(!filter(trees, inverse(TreeType.empty)).isEmpty())
            return Literal.empty();

        if (trees.length == 0)
            return Literal.empty();
        if(trees.length == 1)
            return trees[0];

        Tree and = new And();
        and.addChild(trees);
        return and;
    }

    @Override
    public Tree addChild(Tree... trees) {
        if(!filter(trees, inverse(TreeType.empty)).isEmpty())
            return Literal.empty();
        add(TreeType.and, trees);
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

    @Override
    public Tree copy(){
        return copy(new And());
    }
}
