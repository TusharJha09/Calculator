import java.util.Scanner;

public class Calculator {

    // Addition
    static double add(double a, double b) {
        return a + b;
    }

    // Subtraction
    static double subtract(double a, double b) {
        return a - b;
    }

    // Multiplication
    static double multiply(double a, double b) {
        return a * b;
    }

    // Division
    static double divide(double a, double b) {
        if (b == 0.0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }

    // Modulus
    static double modulus(double a, double b) {
        if (b == 0.0) {
            throw new ArithmeticException("Modulus by zero is not allowed.");
        }
        return a % b;
    }

    // Power
    static double power(double a, double b) {
        double result = Math.pow(a, b);

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new ArithmeticException(
                "The result is not a valid finite number."
            );
        }

        return result;
    }

    // Square root
    static double squareRoot(double a) {
        if (a < 0.0) {
            throw new ArithmeticException(
                "Cannot calculate square root of a negative number."
            );
        }

        return Math.sqrt(a);
    }

    // Read a valid number
    static Double readNumber(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);

            if (!sc.hasNextLine()) {
                return null;
            }

            String input = sc.nextLine().trim();

            // Allow user to exit while entering a number
            if (input.equalsIgnoreCase("exit")) {
                return null;
            }

            try {
                double number = Double.parseDouble(input);

                // Prevent NaN and Infinity
                if (Double.isNaN(number) || Double.isInfinite(number)) {
                    System.out.println(
                        "Please enter a finite number.\n"
                    );
                    continue;
                }

                return number;

            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input. Please enter a valid number.\n"
                );
            }
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("        JAVA CALCULATOR");
        System.out.println("=================================");
        System.out.println("Available operations:");
        System.out.println("+     Addition");
        System.out.println("-     Subtraction");
        System.out.println("*     Multiplication");
        System.out.println("/     Division");
        System.out.println("%     Modulus");
        System.out.println("^     Power");
        System.out.println("sqrt  Square Root");
        System.out.println("exit  Exit calculator");
        System.out.println("=================================\n");

        while (true) {

            System.out.print(
                "Enter operator (+, -, *, /, %, ^, sqrt, exit): "
            );

            if (!sc.hasNextLine()) {
                break;
            }

            String operator = sc.nextLine().trim();

            // Exit
            if (operator.equalsIgnoreCase("exit")) {
                break;
            }

            // Ignore empty input
            if (operator.isEmpty()) {
                System.out.println("Please enter an operator.\n");
                continue;
            }

            try {

                double result;

                // Square root
                if (operator.equalsIgnoreCase("sqrt")) {

                    Double number = readNumber(
                        sc,
                        "Enter number: "
                    );

                    if (number == null) {
                        break;
                    }

                    result = squareRoot(number);

                }

                // Binary operations
                else if (
                    operator.equals("+") ||
                    operator.equals("-") ||
                    operator.equals("*") ||
                    operator.equals("/") ||
                    operator.equals("%") ||
                    operator.equals("^")
                ) {

                    Double num1 = readNumber(
                        sc,
                        "Enter first number: "
                    );

                    if (num1 == null) {
                        break;
                    }

                    Double num2 = readNumber(
                        sc,
                        "Enter second number: "
                    );

                    if (num2 == null) {
                        break;
                    }

                    switch (operator) {

                        case "+":
                            result = add(num1, num2);
                            break;

                        case "-":
                            result = subtract(num1, num2);
                            break;

                        case "*":
                            result = multiply(num1, num2);
                            break;

                        case "/":
                            result = divide(num1, num2);
                            break;

                        case "%":
                            result = modulus(num1, num2);
                            break;

                        case "^":
                            result = power(num1, num2);
                            break;

                        default:
                            throw new IllegalStateException(
                                "Unexpected operator."
                            );
                    }

                }

                // Invalid operator
                else {

                    System.out.println(
                        "Invalid operator. Please try again.\n"
                    );

                    continue;
                }

                // Display result
                System.out.printf(
                    "Result: %.4f%n%n",
                    result
                );

            } catch (ArithmeticException e) {

                System.out.println(
                    "Math Error: " + e.getMessage() + "\n"
                );
            }
        }

        System.out.println(
            "\nCalculator closed. Goodbye!"
        );

        sc.close();
    }
}
