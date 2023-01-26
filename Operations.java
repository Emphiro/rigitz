package rigitz;

import java.util.ArrayList;
import java.util.List;

public abstract class Operations extends Tree{
    protected List<Tree> children = new ArrayList<>();





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
}
