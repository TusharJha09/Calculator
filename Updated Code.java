
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.util.ArrayList;

public class Calculator extends JFrame {

    private final Color bg = new Color(12, 16, 30);
    private final Color panel = new Color(23, 30, 49);
    private final Color cyan = new Color(0, 225, 210);
    private final Color purple = new Color(145, 95, 255);
    private final Color white = new Color(240, 245, 255);

    private JLabel display;
    private JLabel expression;
    private JPanel historyPanel;

    private String input = "0";
    private String operator = "";
    private double first = 0;
    private double answer = 0;
    private boolean newInput = true;

    private final ArrayList<String> history = new ArrayList<>();

    private float glow = 0;

    public Calculator() {

        setTitle("NEXUS - Animated Calculator");
        setSize(800, 600);
        setMinimumSize(new Dimension(650, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createGUI();

        Timer animation = new Timer(40, e -> {
            glow += 0.05f;
            if (glow > 6.28f) glow = 0;
            repaint();
        });

        animation.start();
    }

    private void createGUI() {

        JPanel main = new JPanel(new BorderLayout(15, 15)) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(
                    0, 0, bg,
                    getWidth(), getHeight(),
                    new Color(35, 20, 65)
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(0, 225, 210, 20));
                int x = (int)(100 + 50 * Math.sin(glow));
                g2.fillOval(x, 80, 200, 200);
            }
        };

        main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("N E X U S");
        title.setForeground(cyan);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitle = new JLabel("ADVANCED SCIENTIFIC CALCULATOR");
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(subtitle);

        JPanel calculator = new JPanel(new BorderLayout(10, 12));
        calculator.setBackground(panel);
        calculator.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel screen = new JPanel(new GridLayout(2, 1));
        screen.setBackground(new Color(15, 22, 38));
        screen.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        expression = new JLabel("READY TO CALCULATE");
        expression.setForeground(Color.GRAY);
        expression.setHorizontalAlignment(SwingConstants.RIGHT);

        display = new JLabel("0");
        display.setForeground(cyan);
        display.setFont(new Font("Monospaced", Font.BOLD, 38));
        display.setHorizontalAlignment(SwingConstants.RIGHT);

        screen.add(expression);
        screen.add(display);

        String[][] keys = {
            {"AC", "DEL", "√", "^", "÷"},
            {"7", "8", "9", "!", "×"},
            {"4", "5", "6", "%", "-"},
            {"1", "2", "3", "π", "+"},
            {"±", "0", ".", "HIST", "="}
        };

        JPanel buttons = new JPanel(new GridLayout(5, 5, 9, 9));
        buttons.setOpaque(false);

        for (String[] row : keys) {
            for (String key : row) {
                JButton b = new JButton(key);

                b.setFont(new Font("Arial", Font.BOLD, 17));
                b.setForeground(white);
                b.setBackground(getButtonColor(key));
                b.setFocusPainted(false);
                b.setBorderPainted(false);
                b.setCursor(new Cursor(Cursor.HAND_CURSOR));

                b.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        b.setBackground(cyan);
                        b.setForeground(bg);
                    }

                    public void mouseExited(MouseEvent e) {
                        b.setBackground(getButtonColor(key));
                        b.setForeground(white);
                    }
                });

                b.addActionListener(e -> handle(key));
                buttons.add(b);
            }
        }

        calculator.add(screen, BorderLayout.NORTH);
        calculator.add(buttons, BorderLayout.CENTER);

        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(panel);
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        historyPanel.setPreferredSize(new Dimension(165, 100));

        JLabel historyTitle = new JLabel("HISTORY");
        historyTitle.setForeground(purple);
        historyTitle.setFont(new Font("Arial", Font.BOLD, 14));
        historyPanel.add(historyTitle);

        main.add(header, BorderLayout.NORTH);
        main.add(calculator, BorderLayout.CENTER);
        main.add(historyPanel, BorderLayout.EAST);

        setContentPane(main);

        setupKeyboard();
    }

    private Color getButtonColor(String key) {

        if (key.equals("=")) return new Color(0, 145, 135);

        if (key.equals("AC") || key.equals("DEL"))
            return new Color(130, 45, 70);

        if ("÷×-+^√!%π".contains(key))
            return new Color(75, 60, 130);

        return new Color(38, 49, 73);
    }

    private void handle(String key) {

        try {

            if (key.matches("[0-9]")) {
                if (newInput || input.equals("0")) input = key;
                else if (input.length() < 20) input += key;
                newInput = false;
                updateDisplay();
            }

            else if (key.equals(".")) {
                if (newInput) {
                    input = "0";
                    newInput = false;
                }
                if (!input.contains(".")) input += ".";
                updateDisplay();
            }

            else if (key.equals("AC")) {
                clear();
            }

            else if (key.equals("DEL")) {
                if (input.length() > 1)
                    input = input.substring(0, input.length() - 1);
                else input = "0";
                updateDisplay();
            }

            else if (key.equals("±")) {
                input = input.startsWith("-")
                        ? input.substring(1) : "-" + input;
                updateDisplay();
            }

            else if (key.equals("π")) {
                input = String.valueOf(Math.PI);
                newInput = true;
                updateDisplay();
            }

            else if ("+-×÷^".contains(key) && key.length() == 1) {
                first = Double.parseDouble(input);
                operator = key;
                expression.setText(input + " " + operator);
                newInput = true;
            }

            else if (key.equals("=")) {
                calculate();
            }

            else if (key.equals("√")) {
                double n = Double.parseDouble(input);
                if (n < 0) throw new ArithmeticException("Invalid square root");
                input = String.valueOf(Math.sqrt(n));
                expression.setText("√(" + n + ")");
                newInput = true;
                updateDisplay();
            }

            else if (key.equals("!")) {
                int n = Integer.parseInt(input);
                if (n < 0 || n > 170)
                    throw new ArithmeticException("Enter integer from 0 to 170");

                double result = 1;
                for (int i = 2; i <= n; i++) result *= i;

                input = String.valueOf(result);
                expression.setText(n + "!");
                newInput = true;
                updateDisplay();
            }

            else if (key.equals("%")) {
                input = String.valueOf(
                    Double.parseDouble(input) / 100
                );
                expression.setText("Percentage");
                newInput = true;
                updateDisplay();
            }

            else if (key.equals("HIST")) {
                showHistory();
            }

        } catch (Exception ex) {
            display.setText("ERROR");
            expression.setText(ex.getMessage());
            input = "0";
            operator = "";
            newInput = true;
        }
    }

    private void calculate() {

        if (operator.isEmpty()) return;

        double second = Double.parseDouble(input);
        double result;

        switch (operator) {
            case "+": result = first + second; break;
            case "-": result = first - second; break;
            case "×": result = first * second; break;
            case "÷":
                if (second == 0) throw new ArithmeticException("Cannot divide by zero");
                result = first / second;
                break;
            case "^": result = Math.pow(first, second); break;
            default: return;
        }

        if (!Double.isFinite(result))
            throw new ArithmeticException("Result out of range");

        String oldExpression = first + " " + operator + " " + second;

        answer = result;
        input = BigDecimal.valueOf(result)
                .stripTrailingZeros().toPlainString();

        expression.setText(oldExpression + " =");

        history.add(0, oldExpression + " = " + input);

        if (history.size() > 10) history.remove(history.size() - 1);

        refreshHistory();

        operator = "";
        newInput = true;
        updateDisplay();

        animateResult();
    }

    private void animateResult() {

        Timer timer = new Timer(80, null);
        final int[] count = {0};

        timer.addActionListener(e -> {
            display.setForeground(
                count[0] % 2 == 0 ? Color.WHITE : cyan
            );

            count[0]++;

            if (count[0] >= 6) {
                display.setForeground(cyan);
                timer.stop();
            }
        });

        timer.start();
    }

    private void clear() {
        input = "0";
        first = 0;
        operator = "";
        newInput = true;
        expression.setText("READY TO CALCULATE");
        updateDisplay();
    }

    private void updateDisplay() {
        display.setText(input);
        display.setForeground(cyan);
    }

    private void refreshHistory() {

        historyPanel.removeAll();

        JLabel heading = new JLabel("HISTORY");
        heading.setForeground(purple);
        heading.setFont(new Font("Arial", Font.BOLD, 14));
        historyPanel.add(heading);

        for (String item : history) {
            JLabel label = new JLabel("<html>" + item + "</html>");
            label.setForeground(white);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            historyPanel.add(label);
        }

        historyPanel.revalidate();
        historyPanel.repaint();
    }

    private void showHistory() {

        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No history available!");
            return;
        }

        JOptionPane.showMessageDialog(
            this,
            String.join("\n\n", history),
            "Calculation History",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void setupKeyboard() {

        String[][] keys = {
            {"0", "0"}, {"1", "1"}, {"2", "2"},
            {"3", "3"}, {"4", "4"}, {"5", "5"},
            {"6", "6"}, {"7", "7"}, {"8", "8"},
            {"9", "9"}, {"+", "+"}, {"-", "-"},
            {"*", "×"}, {"/", "÷"}, {"ENTER", "="},
            {"BACK_SPACE", "DEL"}, {"ESCAPE", "AC"}
        };

        for (String[] key : keys) {

            getRootPane().getInputMap(
                JComponent.WHEN_IN_FOCUSED_WINDOW
            ).put(KeyStroke.getKeyStroke(key[0]), key[0]);

            getRootPane().getActionMap().put(
                key[0],
                new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        handle(key[1]);
                    }
                }
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {}

            new Calculator().setVisible(true);
        });
    }
}
