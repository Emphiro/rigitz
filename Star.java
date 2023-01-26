package rigitz;

import java.util.ArrayList;
import java.util.List;

public class Star extends Tree {
    private Tree child;
    private Star(Tree tree){
        this.child = tree;
    }

    public static Tree create(Tree tree){
        if(tree == Literal.empty() || tree == Literal.epsilon())
            return Literal.empty();

        return new Star(tree);
    }

    @Override
    public List<Tree> getChildren() {
        ArrayList<Tree> result = new ArrayList<>();
        result.add(child);
        return result;
    }

    @Override
    public int getNumChildren() {
        return 1;
    }

    @Override
    public Tree addChild(Tree... trees) {
        return this;
    }

    @Override
    public String toString() {
        return child.toString() + Tree.starStr;
    }

    @Override
    public TreeType getType(){
        return TreeType.star;
    }
}
