package equation_cheking;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Stack;

public class Calculator {

    final static Logger logger = Logger.getLogger(Calculator.class);
    Stack<MathSign> mathSignsStack = new Stack<MathSign>();
    Stack<Double> operands = new Stack<Double>();

    public double add(double a, double b) {
        return (a + b);
    }

    public double sub(double a, double b) {
        return (a - b);
    }

    public double multiply(double a, double b) {
        return (a * b);
    }

    public double div(double a, double b) {
        return (a / b);
    }

    public double calculate(ArrayList<String>  instruction){
        String mathSigns = "/*-+()";
        for (String token : instruction){
            if(mathSigns.contains(token)){
                addMath(token);
            }
            else{
                addOperand(token);
            }

        }
        while (!mathSignsStack.isEmpty()){
            if (!operands.isEmpty()){
                compute();
            }
        }
        double result;
        if (operands.size() == 1){
            result = operands.pop();
        }
        else{
            result = 0;
            logger.error("Check your operands");
        }
        return result;
    }

    public void addMath(String sign){
        switch (sign){
            case "(":
                MathSign tmp = new MathSign(sign, 0);
                addToMathStack(tmp);
                break;
            case ")":
                while(mathSignsStack.peek().sign != "("){
                    compute();
                }
                mathSignsStack.pop();
                break;
            case "-":
            case "+":
                tmp = new MathSign(sign, 1);
                addToMathStack(tmp);
                break;
            case "*":
                tmp = new MathSign(sign, 2);
                addToMathStack(tmp);
                break;
            case "/":
                tmp = new MathSign(sign, 2);
                addToMathStack(tmp);
                break;
            default:
                logger.error("Unexpected math sign");
        }

    }

    public void addOperand(String operand){
        try {
            double tmp = Double.parseDouble(operand);
            operands.push(tmp);
        }catch (NumberFormatException e){
            logger.error("check numbers");
            mathSignsStack.clear();
            operands.clear();
        }

    }

    public void compute(){
        double x,y;
        if (operands.size() < 2){
            mathSignsStack.clear();
            operands.clear();
            logger.error("not enough operands");
        }
        else {
            y = operands.pop();
            x = operands.pop();
            MathSign sign = mathSignsStack.pop();
            switch (sign.sign){
                case "*":
                    double res = multiply(x, y);
                    operands.push(res);
                    break;
                case "/":
                    if(y != 0){
                        res = div(x, y);
                        operands.push(res);
                    }
                    else{
                        logger.error("cannot be divided by zero");
                        mathSignsStack.clear();
                        operands.clear();
                    }
                    break;
                case "+":
                    res = add(x, y);
                    operands.push(res);
                    break;
                case "-":
                    res = sub(x, y);
                    operands.push(res);
                    break;
                default:
                    logger.error("unexpected operator");
                    mathSignsStack.clear();
                    operands.clear();

            }

        }



    }

    public void addToMathStack(MathSign sign){
        if (mathSignsStack.isEmpty()){
            mathSignsStack.push(sign);
        }
        else if (mathSignsStack.peek().priority < sign.priority || sign.sign.equals("(")){
            mathSignsStack.push(sign);
        }
        else {
            compute();
            mathSignsStack.push(sign);
        }

    }
}
