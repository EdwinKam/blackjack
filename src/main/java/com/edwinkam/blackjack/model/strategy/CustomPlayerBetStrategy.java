package com.edwinkam.blackjack.model.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;


@AllArgsConstructor
@Setter
@Getter
public class CustomPlayerBetStrategy {
    private String leftValue;
    private String comparisonOperator;
    private String rightValue;
    private String bet;

    private double eval(String value, int currentRunningCount) throws Exception {
        try {
            String replacedValue = value.replace("runningCount", String.valueOf(currentRunningCount));
           return  evaluateExpression(replacedValue);
        } catch (Exception e) {
            String replacedValue = value.replace("runningCount", String.valueOf(currentRunningCount));
            throw new Exception("Error trying to eval '" + value + "'. Replaced value: '" + replacedValue + "'", e);
        }
    }

    private double evaluateExpression(String expr) {
        expr = expr.replaceAll(" ", ""); // Remove all spaces
        char[] tokens = expr.toCharArray();

        // Stack for numbers
        Stack<Double> values = new Stack<>();

        // Stack for Operators
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if ((tokens[i] >= '0' && tokens[i] <= '9') || (tokens[i] == '-' && (i == 0 || !(tokens[i - 1] >= '0' && tokens[i - 1] <= '9')))) {
                StringBuilder buffer = new StringBuilder();
                while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.' || (tokens[i] == '-' && (i == 0 || !(tokens[i - 1] >= '0' && tokens[i - 1] <= '9'))))) {
                    buffer.append(tokens[i++]);
                }
                values.push(Double.parseDouble(buffer.toString()));
                i--; // Back off by 1 since the outer loop will increment it
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOperation(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }

        while (!ops.empty()) {
            values.push(applyOperation(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        } else {
            return true;
        }
    }

    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    public int getLeftValue(int currentRunningCount) throws Exception {
        return (int) eval(leftValue, currentRunningCount);
    }

    public int getRightValue(int currentRunningCount) throws Exception {
        return (int) eval(rightValue, currentRunningCount);
    }

    public boolean conditionMet(int runningCount) throws Exception {
        switch (comparisonOperator) {
            case "<":
                return getLeftValue(runningCount) < getRightValue(runningCount);
            case ">":
                return getLeftValue(runningCount) > getRightValue(runningCount);
            case "<=":
                return getLeftValue(runningCount) <= getRightValue(runningCount);
            case ">=":
                return getLeftValue(runningCount) >= getRightValue(runningCount);
            case "=":
                return getLeftValue(runningCount) == getRightValue(runningCount);
            default:
                throw new IllegalArgumentException("Invalid comparison operator");
        }
    }

    public double getBet(int currentRunningCount) throws Exception {
        return eval(bet, currentRunningCount);
    }

    public String getComparisonOperator() {
        return comparisonOperator;
    }

    @Override
    public String toString() {
        return "CustomPlayerBetStrategy {" +
                "leftValue = '" + leftValue + '\'' +
                ", comparisonOperator = '" + comparisonOperator + '\'' +
                ", rightValue = '" + rightValue + '\'' +
                ", bet = '" + bet + '\'' +
                '}';
    }
}

