package rigitz;

import java.util.ArrayList;
import java.util.List;

public class Literal extends Tree{
    private static Tree epsilon;
    private static Tree empty;
    private static TreeType type = TreeType.literal;
    private String lit;
    private Literal(String lit){
        this.lit = lit;
    }
    public static Tree create(String lit){
        return new Literal(lit);
    }
    public static List<Tree> create(String... lit){
        List<Tree> result = new ArrayList<>();
        for (int i = 0; i < lit.length; i++) {
            if(lit[i] != null)
                result.add(Literal.create(lit[i]));
        }
        return result;
    }
    public static List<Tree> create(List<String> lit){
        return create(lit.toArray(new String[0]));
    }

    private Literal(TreeType type){
        switch (type){
            case epsilon:
                this.lit = Tree.epsilonStr;
                this.type = TreeType.epsilon;
                break;
            case empty:
                this.lit = "";
                this.type = TreeType.empty;
                break;
            default:
                throw new UnsupportedOperationException("This Constructor is only for creating Singletons");
        }
        if(type == TreeType.epsilon) {
            this.lit = Tree.epsilonStr;
            this.type = TreeType.epsilon;
        }

    }
    public static Tree epsilon(){
        if(epsilon == null){
            epsilon = new Literal(TreeType.epsilon);
        }
        return epsilon;
    }

    public static Tree empty(){
        if(empty == null){
            empty = new Literal(TreeType.empty);
        }
        return empty;
    }

    @Override
    public List<Tree> getChildren() {
        return null;
    }

    @Override
    public int getNumChildren() {
        return 0;
    }

    @Override
    public Tree addChild(Tree... tree) {
        return this;
    }

    @Override
    public String toString() {
        return lit;
    }
}
