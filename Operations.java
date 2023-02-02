package rigitz;

import java.util.ArrayList;
import java.util.List;

public abstract class Operations extends Tree{
    protected List<Tree> children = new ArrayList<>();


    public Tree removeChild(int i){
        children.remove(i);
        if(children.size() == 1)
            return children.get(0);
        return this;
    }



    @Override
    public List<Tree> getChildren() {
        return children;
    }

    @Override
    public int getNumChildren() {
        return children.size();
    }


    public String toString(String opStr) {
        if(isEmpty())
            return Tree.emptyStr;
        StringBuilder sb = new StringBuilder("(");
        sb.append(children.get(0).toString());
        for(int i = 1; i < getNumChildren(); i++) {
            sb.append(opStr).append(children.get(i).toString());
        }
        sb.append(")");
        return sb.toString();
    }

    public Tree add(TreeType tt, List<Tree> trees){
        return add(tt, trees.toArray(new Tree[0]));
    }
    public Tree add(TreeType tt, Tree... trees){
        for (int i = 0; i < trees.length; i++) {
            if(trees[i].getType() == tt){
                for (Tree t : trees[i].getChildren())
                    children.add(t);
            }else{
                children.add(trees[i]);
            }
        }
        return this;
    }
    public Tree copy(Tree type){
        for (Tree t : children){
            type.addChild(t.copy());
        }
        return type;
    }
}
