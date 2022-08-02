

public class Calculator {

    public static String toStringInArabic(String number) throws ExpressionException {
        if(number.length() > 4) {
            throw new ExpressionException("One of two Roman numbers is bigger than 10");
        }
        switch(number)
        {
            case "I":
                return "1";
            case "II":
                return "2";
            case "III":
                return "3";
            case "IV":
                return "4";
            case "V":
                return "5";
            case "VI":
                return "6";
            case "VII":
                return "7";
            case "VIII":
                return "8";
            case "IX":
                return "9";
            case "X":
                return "10";
            default:
                throw new ExpressionException("Invalid Roman number");
        }
    }


    private static final int[] ARABIC_NUMBERS = {
            100,   90,  50,   40,   10,
            9,     5,   4,    1
    };
    private static final String[] ROMAN_LETTERS = {
            "C",  "XC", "L",  "XL",  "X",
            "IX", "V",  "IV", "I"
    };
    public static String toStringInRoman(String number) throws ExpressionException {
        int num = Integer.parseInt(number);
        if(num < 1) throw new ExpressionException("Result must be greater than zero");
        String result = "";
        for (int i = 0; i < ARABIC_NUMBERS.length; ++i) {
            while (num >= ARABIC_NUMBERS[i]) {
                result += ROMAN_LETTERS[i];
                num -= ARABIC_NUMBERS[i];
            }
        }
        return result;
    }


    private static class ExpressionException extends Exception {
        ExpressionException(String message) {
            super(message);
        }
    }


    public static String calc(String expression) throws ExpressionException {
         if(expression == null || expression.length() < 3) {
             throw new ExpressionException("Incomplete expression");
         }

         String order = "";
         final char ARABIC_NUM = 'a', ROMAN_NUM = 'r', OPERATION = 'o';

         for(int i = 0; i < expression.length(); ++i) {
            if(expression.codePointAt(i) >= '0' && expression.codePointAt(i) <= '9') {
                order += ARABIC_NUM;
                continue;
            }

            switch (expression.charAt(i))
            {
                case 'I', 'V', 'X':
                    order += ROMAN_NUM;
                    break;
                case '+', '-', '*', '/':
                    order += OPERATION;
                    break;
                case ' ':
                    break;
                default:
                    throw new ExpressionException("Have an invalid character");
            }
        }

        int operationCounter = 0;
        for(int i = 0; i < order.length(); ++i) {
            if(order.charAt(i) == OPERATION) ++operationCounter;
        }

        if(operationCounter == 0) {
            throw new ExpressionException("Haven't any operation");
        }

        if(operationCounter > 1) {
            throw new ExpressionException("Have an extra operation symbol");
        }

        final int INDEX_OF_OPERATION_IN_ORDER = order.indexOf(OPERATION);
        if(order.charAt(0) == OPERATION || INDEX_OF_OPERATION_IN_ORDER == order.length() - 1) {
            throw new ExpressionException("Have an invalid order of operation or\n" +
                    "One of the numbers is missing");
        }

        for(int i = 0; i < order.length(); ++i) {
            if(order.charAt(i) == order.charAt(0) || order.charAt(i) == OPERATION) {
                continue;
            }
            throw new ExpressionException("Mixing Arabic and Roman numerals");
        }

        String cleanExpression = "";
        // Eliminate space characters in expression
        for(int i = 0; i < expression.length(); ++i) {
            if(expression.charAt(i) != ' ') {
                cleanExpression += expression.charAt(i);
            }
        }

        // Length of String expression without spaces == length of String order
        final char OPERATION_IN_EXPRESSION = cleanExpression.charAt(INDEX_OF_OPERATION_IN_ORDER);
        String a = cleanExpression.substring(0, INDEX_OF_OPERATION_IN_ORDER),
                b = cleanExpression.substring(INDEX_OF_OPERATION_IN_ORDER + 1);

        // Translate Roman in Arabic
        if(order.charAt(0) == ROMAN_NUM) {
            a = toStringInArabic(a);
            b = toStringInArabic(b);
        }

        if(Integer.parseInt(a) > 10 || Integer.parseInt(a) < 1 ||
                Integer.parseInt(b) > 10 || Integer.parseInt(b) < 1) {
            throw new ExpressionException("Arabic numerals must be from 1 to 10");
        }


        // Calculate, FINALLY...
        String result = "";
        switch (OPERATION_IN_EXPRESSION)
        {
            case '+':
                result += Integer.parseInt(a) + Integer.parseInt(b);
                break;
            case '-':
                result += Integer.parseInt(a) - Integer.parseInt(b);
                break;
            case '*':
                result += Integer.parseInt(a) * Integer.parseInt(b);
                break;
            case '/':
                result += Integer.parseInt(a) / Integer.parseInt(b);
                break;
        }
        return order.charAt(0) != ROMAN_NUM ? result : toStringInRoman(result);
    }


    public static void main(String[] args) {
        if(args == null || args.length != 1) {
            System.out.println("Calculator \"EXPRESSION\" - to use\n" +
                    "Calculator /? - to learn the command");
            return;
        }

        if(args[0].equals("/?")) {
            System.out.println("\nThe program takes as input an expression, \n" +
                    "containing two numbers and the action between them, \n" +
                    "each number ranging from 1 to 10. \n" +
                    "Examples of expressions are: \"7 + 9\", \"0 - 1\", \"5 * 5\", \"3 / 1\", \n" +
                    "\"III + IV\", \"IX - VIII\".\n" +
                    "The program returns the result of the action.\n" +
                    "The numbers in the expression can only be Arabic or only Roman, \n" +
                    "but not different as here: \"3 + V\", \"II - 1\".\n" +
                    "The expression must have: '+', '-', '*', '/', 'I', 'V', 'X', space\n" +
                    "The program ignores the space character\n");
            return;
        }

        try {
            System.out.println(calc(args[0]));
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
