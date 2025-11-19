import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorGUI extends JFrame implements ActionListener {
    private JTextField display;
    private StringBuilder currentInput;
    private double storedValue = 0;
    private String pendingOp = "";

    public CalculatorGUI() {
        setTitle("Kalkulator Sederhana");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // Display
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(display, BorderLayout.NORTH);

        currentInput = new StringBuilder();

        // Panel tombol
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
            "C", "(", ")", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", "±"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.addActionListener(this);
            panelButtons.add(btn);
        }

        add(panelButtons, BorderLayout.CENTER);

        // Footer (opsional)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel info = new JLabel("Simple Java Swing Calculator");
        footer.add(info);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "C":
                currentInput.setLength(0);
                storedValue = 0;
                pendingOp = "";
                display.setText("");
                break;
            case "=":
                computePending();
                pendingOp = "";
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                handleOperator(cmd);
                break;
            case "±":
                toggleSign();
                break;
            case "(":
            case ")":
                // Tidak memproses parenthesis pada versi sederhana ini
                break;
            default:
                // angka atau titik
                if (cmd.matches("[0-9]") || ".".equals(cmd)) {
                    currentInput.append(cmd);
                    display.setText(currentInput.toString());
                }
                break;
        }
    }

    private void handleOperator(String op) {
        try {
            if (currentInput.length() > 0) {
                double val = Double.parseDouble(currentInput.toString());
                if (pendingOp.isEmpty()) {
                    storedValue = val;
                } else {
                    storedValue = applyOp(storedValue, val, pendingOp);
                }
                display.setText(trimDouble(storedValue));
                currentInput.setLength(0);
            }
            pendingOp = op;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            currentInput.setLength(0);
        }
    }

    private void computePending() {
        try {
            if (!pendingOp.isEmpty() && currentInput.length() > 0) {
                double val = Double.parseDouble(currentInput.toString());
                storedValue = applyOp(storedValue, val, pendingOp);
                display.setText(trimDouble(storedValue));
                currentInput.setLength(0);
            }
        } catch (ArithmeticException ae) {
            display.setText("Error: " + ae.getMessage());
            currentInput.setLength(0);
        } catch (NumberFormatException ex) {
            display.setText("Error");
            currentInput.setLength(0);
        }
    }

    private double applyOp(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            default: return b;
        }
    }

    private void toggleSign() {
        if (currentInput.length() > 0) {
            if (currentInput.charAt(0) == '-') {
                currentInput.deleteCharAt(0);
            } else {
                currentInput.insert(0, '-');
            }
            display.setText(currentInput.toString());
        } else if (display.getText().length() > 0) {
            try {
                double v = Double.parseDouble(display.getText());
                v = -v;
                display.setText(trimDouble(v));
                currentInput.setLength(0);
            } catch (NumberFormatException ex) {
                // ignore
            }
        }
    }

    private String trimDouble(double d) {
        if (d == (long) d) return String.format("%d", (long) d);
        else return String.format("%s", d);
    }

    public static void main(String[] args) {
        // Jalankan GUI di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new CalculatorGUI());
    }
}