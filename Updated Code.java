
import java.util.Locale;
import java.util.Scanner;

public class Calculator {

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Scanner;

public class Calculator {

    private static final Scanner SCANNER = new Scanner(System.in);

    // Precision for division, square root, and fractional powers
    private static final MathContext MC =
            new MathContext(34, RoundingMode.HALF_EVEN);

    private static final int MAX_FACTORIAL = 1000;
    private static final int MAX_INTEGER_POWER = 10000;

    // ================= BASIC OPERATIONS =================

    private static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    private static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    private static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    private static BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.signum() == 0) {
            throw new ArithmeticException(
                    "Division by zero is not allowed.");
        }

        return a.divide(b, MC);
    }

    private static BigDecimal modulus(BigDecimal a, BigDecimal b) {
        if (b.signum() == 0) {
            throw new ArithmeticException(
                    "Modulus by zero is not allowed.");
        }

        return a.remainder(b);
    }

    // ================= ADVANCED OPERATIONS =================

    private static BigDecimal squareRoot(BigDecimal number) {
        if (number.signum() < 0) {
            throw new ArithmeticException(
                    "Square root of a negative number is not real.");
        }

        return number.sqrt(MC);
    }

    // Example: 20% of 500 = 100
    private static BigDecimal percentage(
            BigDecimal value, BigDecimal percent) {

        return value.multiply(percent)
                    .divide(BigDecimal.valueOf(100), MC);
    }

    // Calculates exact factorials using BigInteger
    private static BigDecimal factorial(BigDecimal number) {
        if (number.signum() < 0
                || number.stripTrailingZeros().scale() > 0) {
            throw new ArithmeticException(
                    "Factorial requires a non-negative integer.");
        }

        if (number.compareTo(
                BigDecimal.valueOf(MAX_FACTORIAL)) > 0) {
            throw new ArithmeticException(
                    "Maximum supported factorial input is "
                            + MAX_FACTORIAL + ".");
        }

        int n = number.intValueExact();
        BigInteger result = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        return new BigDecimal(result);
    }

    // Integer powers use BigDecimal.
    // Fractional powers use Java's Math.pow().
    private static BigDecimal power(
            BigDecimal base, BigDecimal exponent) {

        if (exponent.stripTrailingZeros().scale() <= 0) {
            int n;

            try {
                n = exponent.intValueExact();
            } catch (ArithmeticException e) {
                throw new ArithmeticException(
                        "Integer exponent is outside the supported range.");
            }

            if (Math.abs((long) n) > MAX_INTEGER_POWER) {
                throw new ArithmeticException(
                        "Integer exponent must be between -"
                                + MAX_INTEGER_POWER + " and "
                                + MAX_INTEGER_POWER + ".");
            }

            if (n < 0 && base.signum() == 0) {
                throw new ArithmeticException(
                        "Zero cannot have a negative exponent.");
            }

            if (n >= 0) {
                return base.pow(n);
            }

            return BigDecimal.ONE.divide(base.pow(-n), MC);
        }

        if (base.signum() < 0) {
            throw new ArithmeticException(
                    "Fractional powers of negative bases "
                            + "are not supported.");
        }

        if (base.signum() == 0 && exponent.signum() < 0) {
            throw new ArithmeticException(
                    "Zero cannot have a negative exponent.");
        }

        double result = Math.pow(
                base.doubleValue(), exponent.doubleValue());

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Power result is outside the supported range.");
        }

        return BigDecimal.valueOf(result);
    }

    // ================= INPUT VALIDATION =================

    private static BigDecimal readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);

            if (!SCANNER.hasNextLine()) {
                return null;
            }

            String input = SCANNER.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                return null;
            }

            if (input.isEmpty()) {
                System.out.println(
                        "Input cannot be empty. Please try again.\n");
                continue;
            }

            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid number. Enter a valid decimal number.\n");
            }
        }
    }

    // ================= CALCULATION DISPATCHER =================

    private static BigDecimal calculate(
            String operator, BigDecimal a, BigDecimal b) {

        switch (operator.toLowerCase(Locale.ROOT)) {
            case "+":
                return add(a, b);

            case "-":
                return subtract(a, b);

            case "*":
                return multiply(a, b);

            case "/":
                return divide(a, b);

            case "%":
                return modulus(a, b);

            case "^":
                return power(a, b);

            case "percent":
                return percentage(a, b);

            default:
                throw new IllegalArgumentException(
                        "Unsupported operator: " + operator);
        }
    }

    // ================= DISPLAY MENU =================

    private static void displayMenu() {
        System.out.println("\n======================================");
        System.out.println("          JAVA CALCULATOR");
        System.out.println("======================================");
        System.out.println(" +       Addition");
        System.out.println(" -       Subtraction");
        System.out.println(" *       Multiplication");
        System.out.println(" /       Division");
        System.out.println(" %       Modulus / Remainder");
        System.out.println(" ^       Power");
        System.out.println(" sqrt    Square root");
        System.out.println(" !       Factorial");
        System.out.println(" percent Percentage");
        System.out.println(" exit    Close calculator");
        System.out.println("======================================");
        System.out.println(
                "Tip: Type 'exit' at any prompt to quit.");
    }

    // Displays results without unnecessary trailing zeros
    private static String formatResult(BigDecimal result) {
        if (result.signum() == 0) {
            return "0";
        }

        return result.stripTrailingZeros().toPlainString();
    }

    // ================= MAIN METHOD =================

    public static void main(String[] args) {
        displayMenu();

        try {
            while (true) {
                System.out.print("\nEnter operator: ");

                if (!SCANNER.hasNextLine()) {
                    break;
                }

                String operator = SCANNER.nextLine()
                        .trim()
                        .toLowerCase(Locale.ROOT);

                if (operator.equals("exit")) {
                    break;
                }

                if (operator.isEmpty()) {
                    System.out.println(
                            "Please enter an operator.");
                    continue;
                }

                try {
                    BigDecimal result;

                    // Unary operations: sqrt and factorial
                    if (operator.equals("sqrt")
                            || operator.equals("!")) {

                        BigDecimal number =
                                readNumber("Enter number: ");

                        if (number == null) {
                            break;
                        }

                        if (operator.equals("sqrt")) {
                            result = squareRoot(number);
                        } else {
                            result = factorial(number);
                        }

                    } else if (operator.equals("+")
                            || operator.equals("-")
                            || operator.equals("*")
                            || operator.equals("/")
                            || operator.equals("%")
                            || operator.equals("^")
                            || operator.equals("percent")) {

                        String firstPrompt =
                                operator.equals("percent")
                                ? "Enter base value: "
                                : "Enter first number: ";

                        BigDecimal first = readNumber(firstPrompt);

                        if (first == null) {
                            break;
                        }

                        String secondPrompt =
                                operator.equals("percent")
                                ? "Enter percentage: "
                                : "Enter second number: ";

                        BigDecimal second = readNumber(secondPrompt);

                        if (second == null) {
                            break;
                        }

                        result = calculate(operator, first, second);

                    } else {
                        System.out.println(
                                "Invalid operator. Choose an operator "
                                        + "from the menu.");
                        continue;
                    }

                    System.out.println(
                            "Result: " + formatResult(result));

                } catch (ArithmeticException
                        | IllegalArgumentException e) {

                    System.out.println(
                            "Error: " + e.getMessage());
                }
            }
        } finally {
            SCANNER.close();
        }

        System.out.println(
                "\nCalculator closed. Goodbye!");
    }
}

    private static final Scanner SCANNER = new Scanner(System.in);

    // Basic arithmetic operations
    private static double add(double a, double b) {
        return a + b;
    }

    private static double subtract(double a, double b) {
        return a - b;
    }

    private static double multiply(double a, double b) {
        return a * b;
    }

    private static double divide(double a, double b) {
        if (b == 0.0) {
            throw new ArithmeticException(
                "Division by zero is not allowed."
            );
        }
        return a / b;
    }

    private static double modulus(double a, double b) {
        if (b == 0.0) {
            throw new ArithmeticException(
                "Modulus by zero is not allowed."
            );
        }
        return a % b;
    }

    // Advanced mathematical operations
    private static double power(double a, double b) {
        return Math.pow(a, b);
    }

    private static double squareRoot(double number) {
        if (number < 0.0) {
            throw new ArithmeticException(
                "Square root of a negative number is not real."
            );
        }
        return Math.sqrt(number);
    }

    // Calculates a percentage of a value
    // Example: 20% of 500 = 100
    private static double percentage(double value, double percent) {
        return (value / 100.0) * percent;
    }

    // Factorial supports non-negative integers up to 170
    private static double factorial(double number) {
        if (number < 0 || number != Math.floor(number)) {
            throw new ArithmeticException(
                "Factorial requires a non-negative integer."
            );
        }

        if (number > 170) {
            throw new ArithmeticException(
                "Factorial is too large. Maximum supported input is 170."
            );
        }

        double result = 1.0;

        for (int i = 2; i <= (int) number; i++) {
            result *= i;
        }

        return result;
    }

    // Prevent infinity and NaN results from being displayed
    private static double validateResult(double result) {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new ArithmeticException(
                "Result is outside the supported numeric range."
            );
        }
        return result;
    }

    // Read and validate a number
    private static Double readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);

            if (!SCANNER.hasNextLine()) {
                return null;
            }

            String input = SCANNER.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                return null;
            }

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.\n");
                continue;
            }

            try {
                double number = Double.parseDouble(input);

                if (Double.isNaN(number)
                        || Double.isInfinite(number)) {
                    System.out.println(
                        "Please enter a finite number.\n"
                    );
                    continue;
                }

                return number;

            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid number. Please try again.\n"
                );
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n================================");
        System.out.println("       JAVA CALCULATOR");
        System.out.println("================================");
        System.out.println(" +       Addition");
        System.out.println(" -       Subtraction");
        System.out.println(" *       Multiplication");
        System.out.println(" /       Division");
        System.out.println(" %       Modulus");
        System.out.println(" ^       Power");
        System.out.println(" sqrt    Square root");
        System.out.println(" !       Factorial");
        System.out.println(" percent Percentage");
        System.out.println(" exit    Close calculator");
        System.out.println("================================");
        System.out.println(
            "Tip: Type exit at any prompt to quit."
        );
    }

    private static double calculate(
            String operator, double a, double b) {

        double result;

        switch (operator.toLowerCase(Locale.ROOT)) {
            case "+":
                result = add(a, b);
                break;

            case "-":
                result = subtract(a, b);
                break;

            case "*":
                result = multiply(a, b);
                break;

            case "/":
                result = divide(a, b);
                break;

            case "%":
                result = modulus(a, b);
                break;

            case "^":
                result = power(a, b);
                break;

            case "percent":
                result = percentage(a, b);
                break;

            default:
                throw new IllegalArgumentException(
                    "Unsupported operator."
                );
        }

        return validateResult(result);
    }

    public static void main(String[] args) {
        displayMenu();

        try {
            while (true) {
                System.out.print("\nEnter operator: ");

                if (!SCANNER.hasNextLine()) {
                    break;
                }

                String operator = SCANNER.nextLine()
                                         .trim()
                                         .toLowerCase(Locale.ROOT);

                if (operator.equals("exit")) {
                    break;
                }

                if (operator.isEmpty()) {
                    System.out.println(
                        "Please enter an operator."
                    );
                    continue;
                }

                try {
                    double result;

                    // Unary operations need only one number
                    if (operator.equals("sqrt")
                            || operator.equals("!")) {

                        Double number = readNumber("Enter number: ");

                        if (number == null) {
                            break;
                        }

                        result = operator.equals("sqrt")
                                ? squareRoot(number)
                                : factorial(number);

                        result = validateResult(result);

                    } else if (
                            operator.equals("+")
                            || operator.equals("-")
                            || operator.equals("*")
                            || operator.equals("/")
                            || operator.equals("%")
                            || operator.equals("^")
                            || operator.equals("percent")) {

                        Double first = readNumber(
                            operator.equals("percent")
                                ? "Enter base value: "
                                : "Enter first number: "
                        );

                        if (first == null) {
                            break;
                        }

                        Double second = readNumber(
                            operator.equals("percent")
                                ? "Enter percentage: "
                                : "Enter second number: "
                        );

                        if (second == null) {
                            break;
                        }

                        result = calculate(operator, first, second);

                    } else {
                        System.out.println(
                            "Invalid operator. Please try again."
                        );
                        continue;
                    }

                    System.out.println(
                        "Result: " + String.format(
                            Locale.ROOT, "%.10g", result
                        )
                    );

                } catch (ArithmeticException e) {
                    System.out.println("Math Error: " + e.getMessage());

                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } finally {
            SCANNER.close();
        }

        System.out.println("\nCalculator closed. Goodbye!");
    }
}
