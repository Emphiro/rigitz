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
    private int depth = 0;
    private boolean dp = true;
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
    static ArrayList<Tree>[][] toArr(Tree[][][] arr){
        ArrayList<Tree>[][] result = new ArrayList[arr.length][arr[0].length];
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
            if(showDepth)
                depth--;
        }
        if (verbose) {
            if(showDepth){
                for (int l = 0; l < depth; l++) {
                    System.out.print(indentation);
                }
            }

            System.out.printf("R(%d, %d, %d) =  %s\n", k, i, j, newRigitz.toString());
        }
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
            System.out.printf("Used R(%d, %d, %d):\n", k, i, j);
        }
        return  dpArray[k][i-1][j-1];
    }
    /*
    private String star(String string){
        if(string.length() == 0)
            return "";
        return string + star;
    }

    private String and(List<String> strings){
        return and(strings.toArray(new String[0]));
    }
    private String and(String... strings){
        String result = "";
        for (int i = 0; i < strings.length; i++) {
            if(strings[i].length() <= 0)
                return "";
        }
        if(strings.length == 0)
            return "";
        result += strings[0];
        for (int i = 1; i < strings.length; i++) {
            result += and + strings[i];
        }
        return result;
    }

    private String union(String... strings){
        return union(Arrays.asList(strings));
    }
    private String union(List<String> strings){
        String result = "";
        ArrayList<String> filteredString = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            if(strings.get(i).length() > 0){
                filteredString.add(strings.get(i));
            }
        }
        if(filteredString.size() == 0)
            return "";
        result += filteredString.get(0);
        for (int i = 1; i < filteredString.size(); i++) {
            result += union + filteredString.get(i);
        }
        return result;
    }

    private String p(String p){
        if(p.length() == 0)
            return "";
        return "(" + p + ")";
    }

     */
}

