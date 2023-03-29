package equation_cheking;

import java.sql.Array;
import java.util.*;

import org.apache.log4j.Logger;


public class EquationCreator {

    final static Logger logger = Logger.getLogger(EquationCreator.class);

    public ArrayList<String> getCorrect_equation() {
        return correct_equation;
    }

    private ArrayList<String> correct_equation;
    Calculator calculator = new Calculator();
    DBWorker dbWorker = new DBWorker();

    public EquationCreator() {
        this.correct_equation = new ArrayList<String>();
    }



    public void menu(){
        final int EXIT_CODE = 0;
        final int ADDING = 1;
        final int SEARCH = 2;
        final int DEFAULT = -1;
        Scanner inScan = new Scanner(System.in);
        while (true){
            int inputCase = DEFAULT;
            System.out.println("0. Exit from app");
            System.out.println("1. Add equation");
            System.out.println("2. SEARCH IN DATABASE");
            if(inScan.hasNextInt()){
                inputCase = inScan.nextInt();
            }
            else {
                inScan.next();
            }
            switch (inputCase){
                case ADDING:
                    getEquationFromTerminal();
                    break;
                case SEARCH:
                    searchInDb();
                    break;
                case EXIT_CODE:
                    System.out.println("Closing app...");
                    System.exit(1);
                case DEFAULT:
                    System.out.println("Enter right choice");


            }
        }

    }

    public void searchInDb(){
        boolean flag = true;
        while (flag){
            System.out.println("3. Get all equations");
            System.out.println("4. Get equations with one root");
            System.out.println("5. Get equations without roots");
            System.out.println("6. Exit from search");
            Scanner in = new Scanner(System.in);
            int choice = 0;
            if (in.hasNextInt()){
                choice = in.nextInt();
                switch (choice){
                    case 3:
                        ArrayList<String> arr = dbWorker.getAllEquations();
                        if (!arr.isEmpty()){
                            System.out.println(arr);
                        }
                        else{
                            System.out.println("We have not equations in DB");
                        }
                        break;
                    case 4:
                        arr = dbWorker.getEquationWithRoots(1);
                        if (!arr.isEmpty()){
                            System.out.println(arr);
                        }
                        else {
                            System.out.println("We have not equations with one root in DB");
                        }
                        break;
                    case 5:
                        arr = dbWorker.getEquationWithRoots(0);
                        if (!arr.isEmpty()){
                            System.out.println(arr);
                        }
                        else {
                            System.out.println("We have not equations without root in DB");
                        }
                        break;
                    case 6:
                        flag = false;
                        break;
                    default:
                        System.out.println("Choose right option");
                }
            }
        }
    }


    public void getEquationFromTerminal(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert equation:");
        String equation = scanner.nextLine();
        logger.info("Introduced equation:" + equation);
        String[] test = equation.split("=");
        boolean flag = true;
        if(test.length != 2){
            flag = false;
            logger.error("Incorrect expression");
        }
        if (!isDigit(test[1])){
            flag = false;
            logger.error("In right side must be number!");
        }
        if (!checkBrackets(test[0])){
            flag = false;
        }
        if (!checkEquation(test[0])){
            flag = false;
            logger.error("Incorrect order of operators  or incorrect operands");
        }
        if (!flag){
            System.out.println("Incorrect equation");
        }
        else {
            System.out.println("Correct equation");
            dbWorker.insertEquation(equation);
            dbWorker.getEquationId(equation);
            addRoot(Double.parseDouble(test[1]), this.correct_equation, dbWorker.getEquationId(equation));
        }

    }

    public void addRoot(double right, ArrayList<String> arr, int equation_id){
        boolean flag = true;
        while (flag){
            System.out.print("Insert equation root? y/n: ");
            Scanner in = new Scanner(System.in);
            String str = in.nextLine();
            if (str.equals("y")){
                if (in.hasNextDouble()){
                    double root = in.nextDouble();
                    int index = arr.indexOf("x");
                    arr.set(index, Double.toString(root));
                    double left = calculator.calculate(arr);
                    if (left == right){
                        System.out.println("Correct root");
                        dbWorker.updateRootCount(equation_id);
                        dbWorker.insertRoot(equation_id, root);
                    }
                    else{
                        System.out.println("Root is not correct");
                    }
                }
                else{
                    in.next();
                }

            }
            else{
                flag = false;
            }

        }

    }

    public boolean isDigit(String temp){
        try{
            Double.parseDouble(temp);
        }catch (NumberFormatException e)
        {
            return false;
        }
        return true;

    }

    public boolean checkBrackets(String equation){
        Stack<String> brackets = new Stack<>() ;
        StringTokenizer tokens = new StringTokenizer(equation, "/*-+()", true);
        while(tokens.hasMoreTokens()){
            String tmp = tokens.nextToken();
            switch (tmp){
                case "(":
                    brackets.push(tmp);
                    break;
                case")":
                    if (!brackets.isEmpty()){
                        brackets.pop();
                    }
                    else {
                        logger.error("Incorrect brackets");
                        return false;
                    }
                    break;

            }
        }
        return brackets.isEmpty();

    }

    public ArrayList<String> tokenizerToArray(StringTokenizer tokenizer){
        ArrayList<String> temp = new ArrayList<>();
        while (tokenizer.hasMoreTokens()){
            temp.add(tokenizer.nextToken());
        }
        return temp;
    }

    public boolean checkBody(int first, ArrayList<String> temp){
        String operators = "/*-+";
        String brackets = "()";
        int size = temp.size();
        for (int i = first; i < size-1; i++) {
            if (isDigit(temp.get(i)) || temp.get(i).equals("x") || brackets.contains(temp.get(i))){
                correct_equation.add(temp.get(i));
            }
            else if (temp.get(i).equals("-") & brackets.contains(temp.get(i-1)) & isDigit(temp.get(i+1))){
                correct_equation.add(temp.get(i) + temp.get(i+1));
                i+=1;
            }
            else if (temp.get(i).equals("-") & operators.contains(temp.get(i-1)) & isDigit(temp.get(i+1))  ){
                correct_equation.add(temp.get(i) + temp.get(i+1));
                i+=1;
            }
            else if (operators.contains(temp.get(i)) & !operators.contains(temp.get(i-1))){
                correct_equation.add(temp.get(i));
            }
            else
            {
                return false;
            }

        }
        if (temp.get(size-1).equals("x") || brackets.contains(temp.get(size-1)) || isDigit(temp.get(size-1))){
            correct_equation.add(temp.get(size-1));
        }
        else{
            logger.error("Last token in left side  incorrect");
            return false;
        }
        return true;
    }

    public boolean checkEquation(String equation){

        StringTokenizer tokenizer= new StringTokenizer(equation,"/*-+())", true);
        ArrayList<String> temp = tokenizerToArray(tokenizer);
        boolean flag = true;
        correct_equation.clear();
        int size = temp.size();
        if (size >= 3){
            if(temp.get(0).equals("-")){
                if(isDigit(temp.get(1))){
                    correct_equation.add(temp.get(0) + temp.get(1));
                    flag = checkBody(2,temp);
                }
            }
            else if (temp.get(0).equals("(") || isDigit(temp.get(0)) || temp.get(0).equals("x")){
                correct_equation.add(temp.get(0));
                flag = checkBody(1,temp);
            }
            else {
                logger.error("Incorrect first token");
                flag =  false;
            }

        }

        return flag;

    }

}
