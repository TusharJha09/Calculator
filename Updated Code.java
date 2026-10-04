
import java.util.Locale;
import java.util.Scanner;

public class Calculator {

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
