import java.util.NoSuchElementException;
import java.util.Scanner;

public class Calculator {

    static double add(double a, double b) { return a + b; }
    static double subtract(double a, double b) { return a - b; }
    static double multiply(double a, double b) { return a * b; }

    static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }

    static double modulus(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Modulus by zero is not allowed.");
        }
        return a % b;
    }

    static double power(double a, double b) {
        return Math.pow(a, b);
    }

    static double squareRoot(double a) {
        if (a < 0) {
            throw new ArithmeticException("Cannot compute square root of a negative number.");
        }
        return Math.sqrt(a);
    }

    // Reads one line and parses it as a double, or returns null on EOF.
    static Double readNumber(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNextLine()) {
                return null; // input stream closed
            }
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.\n");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Java Calculator =====");
        System.out.println("Operations: + - * / % ^ (power) sqrt");
        System.out.println("Type 'exit' anytime to quit.\n");

        while (true) {
            System.out.print("Enter operator (+, -, *, /, %, ^, sqrt, exit): ");

            if (!sc.hasNextLine()) {
                break; // no more input available, exit cleanly
            }
            String operator = sc.nextLine().trim();

            if (operator.equalsIgnoreCase("exit")) {
                break;
            }
            if (operator.isEmpty()) {
                continue;
            }

            try {
                double result;

                if (operator.equals("sqrt")) {
                    Double num = readNumber(sc, "Enter number: ");
                    if (num == null) break;
                    result = squareRoot(num);
                } else if (operator.equals("+") || operator.equals("-") ||
                           operator.equals("*") || operator.equals("/") ||
                           operator.equals("%") || operator.equals("^")) {

                    Double num1 = readNumber(sc, "Enter first number: ");
                    if (num1 == null) break;
                    Double num2 = readNumber(sc, "Enter second number: ");
                    if (num2 == null) break;

                    switch (operator) {
                        case "+": result = add(num1, num2); break;
                        case "-": result = subtract(num1, num2); break;
                        case "*": result = multiply(num1, num2); break;
                        case "/": result = divide(num1, num2); break;
                        case "%": result = modulus(num1, num2); break;
                        case "^": result = power(num1, num2); break;
                        default: throw new IllegalStateException("Unreachable");
                    }
                } else {
                    System.out.println("Invalid operator. Please try again.\n");
                    continue;
                }

                System.out.printf("Result: %.4f%n%n", result);

            } catch (ArithmeticException e) {
                System.out.println("Math error: " + e.getMessage() + "\n");
            }
        }

        System.out.println("Calculator closed. Goodbye!");
        sc.close();
    }
}
