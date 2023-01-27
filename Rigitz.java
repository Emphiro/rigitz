package rigitz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rigitz {
    static final String union = " U ";
    static final String and = "";
    static final String star = "*";
    static final String epsilon = "e";
    static final String indentation = "  ";

    static int statesS = 3;
    static int startS = 1;
    static int[] endsS = {3};
    static String[][][] changesS = {{{"a"}, {"b"}, {}}, {{"a"}, {}, {"b"}}, {{}, {"a"}, {"b"}}};

    private int states;
    private int start;
    private int[] ends;
    private List<String>[][] changes;
    private boolean verbose = true;
    private boolean showDepth = true;
    private boolean dp = true;
    private boolean optimize = true;

    private int depth = 0;
    private Tree[][][] dpArray;


    public Rigitz(int states, int start, int[] ends, String[][][] changes) {
        this.states = states;
        this.start = start;
        this.ends = ends;
        this.changes = toArr(changes);
        if(dp){
            dpArray = new Tree[states+1][states][states];
        }
    }

    public static void main(String... args){
        Rigitz rig = new Rigitz(statesS, startS, endsS, changesS);
        rig.rigitz();
    }

    private static void test(){
        Rigitz rig = new Rigitz(statesS, startS, endsS, changesS);
        System.out.println("//\nTest");
        Tree t = Star.create(Union.create(Literal.create("a"), Tree.epsilon()));
        System.out.println(t);
        System.out.println(rig.optimize(t));
        System.out.println("//");
    }

    static ArrayList<String>[][] toArr(String[][][] arr){
        ArrayList<String>[][] result = new ArrayList[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                result[i][j] = new ArrayList<>();
                for (int k = 0; k < arr[i][j].length; k++) {
                    result[i][j].add(arr[i][j][k]);
                }
            }
        }
        return result;
    }



    public void rigitz(){
        ArrayList<Tree> solutions = new ArrayList<>();
        for (int endstate : ends)
            solutions.add(rigitz(states, start, endstate));
        System.out.println(Union.create(solutions));
    }
    private Tree rigitz(int k, int i, int j){
        Tree newRigitz = Literal.empty();
        Tree optimizedRigitz = null;
        if(k == 0 && i == j){
            List<Tree> withE = new ArrayList<>(Literal.create(changes[i-1][i-1]));
            withE.add(Tree.epsilon());
            newRigitz = Union.create(withE);

        }
        else if(k == 0) {
            newRigitz = Union.create(Literal.create(changes[i-1][j-1]));
        } else {
            if(verbose && showDepth) {
                for (int l = 0; l < depth; l++) {
                    System.out.print(indentation);
                }
                System.out.printf("R(%d, %d, %d):\n", k, i, j);
            }
            if(showDepth)
                depth++;
            newRigitz = calculateNewRigitz(k, i, j);
            if(optimize)
                optimizedRigitz = calculateNewOptimizedRigitz(k, i, j);
            if(showDepth)
                depth--;
        }
        if (verbose) {
            if(showDepth){
                for (int l = 0; l < depth; l++) {
                    System.out.print(indentation);
                }
            }
            if(optimize){
                if(optimizedRigitz == null)
                    optimizedRigitz = optimize(newRigitz);
                System.out.printf("R(%d, %d, %d) =  %s", k, i, j, newRigitz);
                if(!Tree.equals(optimizedRigitz, newRigitz))
                    System.out.printf(" -> %s", optimizedRigitz);
                System.out.println();
                return optimizedRigitz;
            }else{
                System.out.printf("R(%d, %d, %d) =  %s\n", k, i, j, newRigitz);
            }

        }
        if(optimize)
            return optimizedRigitz == null ? optimize(newRigitz) : optimizedRigitz;
        return newRigitz;

    }

    private Tree calculateNewRigitz(int k, int i, int j){
        Tree r1;
        Tree r2;
        Tree r3;
        Tree r4;
        if(!dp) {
            r1 = rigitz(k - 1, i, j);
            r2 = rigitz(k - 1, i, k);
            r3 = rigitz(k - 1, k, k);
            r4 = rigitz(k - 1, k, j);
        } else {
            r1 = calculateDP(k-1, i, j);
            r2 = calculateDP(k-1, i, k);
            r3 = calculateDP(k-1, k, k);
            r4 = calculateDP(k-1, k, j);
        }
        return Union.create(r1, And.create(r2, Star.create(r3), r4));
    }
    private Tree calculateNewOptimizedRigitz(int k, int i, int j){
        Tree r1;
        Tree r2;
        Tree r3;
        Tree r4;
        if(!dp) {
            r1 = rigitz(k - 1, i, j);
            r2 = rigitz(k - 1, i, k);
            r3 = rigitz(k - 1, k, k);
            r4 = rigitz(k - 1, k, j);
        } else {
            r1 = calculateDP(k-1, i, j);
            r2 = calculateDP(k-1, i, k);
            r3 = calculateDP(k-1, k, k);
            r4 = calculateDP(k-1, k, j);
        }
        return optimize(Union.create(r1, optimize(And.create(r2, optimize(Star.create(r3)), r4))));
    }

    private Tree calculateDP(int k, int i, int j){
        if(dpArray[k][i-1][j-1] == null){
            dpArray[k][i-1][j-1] = rigitz(k, i, j);
            return dpArray[k][i-1][j-1];
        }
        if(verbose){
            if(showDepth){
                for (int l = 0; l < depth+1; l++) {
                    System.out.print("  ");
                }
            }
            System.out.printf("Used R(%d, %d, %d): %s\n", k, i, j, dpArray[k][i-1][j-1]);
        }
        return  dpArray[k][i-1][j-1];
    }

    private Tree optimize(Tree tree){
        Tree prev = tree;
        Tree opt = optimizeOnce(tree);
        while(!Tree.equals(prev, opt)){
            prev = opt;
            opt = optimizeOnce(opt);
        }
        return opt;
    }
    private Tree optimizeOnce(Tree tree){

        if(tree.getType() == TreeType.star){
            //System.out.println("opt: found star");
            Star star = (Star)tree;
            // Rule (a U e)* -> a*
            if(star.getChild().getType() == TreeType.union){
                //System.out.println("opt: removing epsilons");
                Union union = (Union)star.getChild();
                return Star.create(union.eliminateEpsilon());
            }
            // Rule ()* ->
            if(star.getChild().getType() == TreeType.empty)
                return Tree.empty();
            if(star.getChild().getType() == TreeType.epsilon)
                return Tree.epsilon();

        }
        // Rule (a U e)a* -> a*
        if(tree.getType() == TreeType.and){
            And and = (And)tree;
            if(tree.getNumChildren() >= 2 && tree.getChildren().get(0).getType() == TreeType.union && tree.getChildren().get(1).getType() == TreeType.star){
                Union union = (Union)tree.getChildren().get(0);
                Star star = (Star)tree.getChildren().get(1);
                Tree a = star.getChild();
                if(union.getNumChildren() == 2){
                    if(union.getChildren().get(0) == Tree.epsilon()){
                        if(Tree.equals(union.getChildren().get(1), a)){
                            return and.removeChild(0);
                        }
                    }
                    if(union.getChildren().get(1) == Tree.epsilon()){
                        if(Tree.equals(union.getChildren().get(0), a)){
                            return and.removeChild(0);
                        }
                    }
                }
            }
        }
        // Rule b U ((a*)b) -> (a*)b
        if(tree.getType() == TreeType.union){
            for (int i = 1; i < tree.getNumChildren(); i++) {
                if(tree.getChildren().get(i).getType() == TreeType.and){
                    And and = (And)tree.getChildren().get(i);
                    if(and.getChildren().get(0).getType() == TreeType.star){
                        Star star = (Star)and.getChildren().get(0);
                        Tree b = And.create(and.getChildren().subList(1, and.getNumChildren()));
                        if(Tree.equals(b, tree.getChildren().get(i-1)))
                            return ((Union)tree).removeChild(i-1);
                    }
                }
            }
        }
        return tree;
    }
}

