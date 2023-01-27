package rigitz;

import java.util.List;

public class Union extends Operations {
    private static final String opString = Tree.unionStr;
    private Union(List<Tree> trees){
        this.addChild(trees);
    }
    private Union(){ }
    public static Tree create(Tree... trees){
        List<Tree> fTrees = filter(trees, TreeType.empty);
        if (fTrees.isEmpty())
            return Literal.empty();
        if(fTrees.size() == 1)
            return fTrees.get(0);
        Tree union = new Union();
        union.addChild(fTrees);
        return union;
    }



    public static Tree create(List<Tree> trees){
        return create(trees.toArray(new Tree[0]));
    }

    public Tree eliminateEpsilon(){
        List<Tree> noEpsilon = filter(children, TreeType.epsilon);
        if(noEpsilon.isEmpty())
            return Tree.empty();
        if(noEpsilon.size() == 1)
            return noEpsilon.get(0);
        this.children = noEpsilon;
        return this;
    }

    @Override
    public Tree addChild(Tree... trees) {
        List<Tree> fTrees = filter(trees, TreeType.empty);
        add(TreeType.union, fTrees);
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
