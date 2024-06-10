import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class Interpolation {

    public static void main(String[] args) {
        Interpolation interpolation = new Interpolation();
        interpolation.run();
    }

    public void run() {
        double[][] data = getData();
        double[] x = data[0];
        double[] y = data[1];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите значение аргумента");
        double xCur = scanner.nextDouble();

        double answer1 = lagrangePolynomial(x, y, xCur);
        double answer2 = newtonPolynomial(x, y, xCur);
        double answer3 = newtonInterpolation(x, y, xCur);
        double answer4 = stirlingInterpolation(x, y, xCur);
        double answer5 = besselInterpolation(x, y, xCur);

        System.out.println("Полином Лагранжа дал ответ: " + answer1);
        System.out.println("Полином Ньютона с разделенными разностями дал ответ: " + answer2);
        System.out.println("Полином Ньютона с конечными разностями дал ответ: " + answer3);
        System.out.println("Многочлен Стирлинга дал ответ: " + answer4);
        System.out.println("Многочлен Бесселя дал ответ: " + answer5);

        // Plotting
        EventQueue.invokeLater(() -> {
            JFrame frame = new PlotFrame(x, y, answer2, answer3, answer4, answer5);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public double[][] getData() {
        Scanner scanner = new Scanner(System.in);
        int num = g(scanner);

        double[] x = new double[100];
        double[] y = new double[100];
        int count = 0;

        try {
            if (num == 1) {
                BufferedReader reader = new BufferedReader(new FileReader("input2.txt"));
                String line;
                while ((line = reader.readLine()) != null && !line.isEmpty()) {
                    String[] parts = line.split(" ");
                    x[count] = Double.parseDouble(parts[0]);
                    y[count] = Double.parseDouble(parts[1]);
                    count++;
                }
                reader.close();
            } else if (num == 2) {
                System.out.println("Введите координаты");
                scanner.nextLine(); // consume the newline
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    String[] parts = line.split(" ");
                    x[count] = Double.parseDouble(parts[0]);
                    y[count] = Double.parseDouble(parts[1]);
                    count++;
                }
            } else {
                System.out.println("1. sin(x)");
                System.out.println("2. x ** 2");
                System.out.println("Выберите уравнение (1 или 2)");
                int n = scanner.nextInt();
                System.out.println("Введите исследуемый интервал");
                scanner.nextLine(); // consume the newline
                String[] interval = scanner.nextLine().split(" ");
                double a = Double.parseDouble(interval[0]);
                double b = Double.parseDouble(interval[1]);
                System.out.println("Введите количество точек на интервале");
                int amount = scanner.nextInt();
                for (int i = 0; i < amount; i++) {
                    double x_i = a + (b - a) * i / amount;
                    x[count] = x_i;
                    if (n == 1) {
                        y[count] = Math.sin(x_i);
                    } else if (n == 2) {
                        y[count] = x_i * x_i;
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new double[][]{Arrays.copyOf(x, count), Arrays.copyOf(y, count)};
    }

    public int g(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите 1, если ввод данных будет происходить из файла. Введите 2, если с клавиатуры. Введите 3 для выбора уравнения");
                int num = scanner.nextInt();
                if (num != 1 && num != 2 && num != 3) {
                    System.out.println("Пожалуйста, введите 1, 2 или 3");
                } else {
                    return num;
                }
            } catch (InputMismatchException e) {
                System.out.println("Пожалуйста, введите число");
                scanner.next(); // consume the invalid input
            }
        }
    }

    public static double lagrangePolynomial(double[] x, double[] y, double xCur) {
        double res = 0;
        for (int i = 0; i < x.length; i++) {
            double p = 1;
            for (int j = 0; j < y.length; j++) {
                if (i != j) {
                    p *= (xCur - x[j]) / (x[i] - x[j]);
                }
            }
            res += p * y[i];
        }
        return res;
    }

    public static double newtonPolynomial(double[] x, double[] y, double xCur) {
        double[] a = newtonCoefficient(x, y);
        int n = x.length - 1;
        double p = a[n];
        for (int k = 1; k <= n; k++) {
            p = a[n - k] + (xCur - x[n - k]) * p;
        }
        return p;
    }

    public static  double[] newtonCoefficient(double[] x, double[] y) {
        int m = x.length;
        double[] yCopy = Arrays.copyOf(y, m);
        for (int k = 1; k < m; k++) {
            for (int j = m - 1; j >= k; j--) {
                yCopy[j] = (yCopy[j] - yCopy[j - 1]) / (x[j] - x[j - k]);
            }
        }
        return yCopy;
    }

    public static double newtonInterpolation(double[] x, double[] y, double xCur) {
        int n = x.length;
        boolean isEquallySpaced = true;
        double h = x[1] - x[0];
        for (int i = 1; i < n - 1; i++) {
            if (Math.round((x[i + 1] - x[i]) * 1000) / 1000.0 != h) {
                isEquallySpaced = false;
                break;
            }
        }
        if (!isEquallySpaced) {
            return Double.NaN; // Узлы не являются равноотстоящими
        }
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++) {
            a[i][0] = y[i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                a[j][i] = a[j + 1][i - 1] - a[j][i - 1];
            }
        }
        double t = (xCur <= x[n / 2]) ? (xCur - x[0]) / h : (xCur - x[n - 1]) / h;
        double result = (xCur <= x[n / 2]) ? a[0][0] : a[n - 1][0];
        for (int i = 1; i < n; i++) {
            result += (tCalc(t, i, xCur <= x[n / 2]) * a[(xCur <= x[n / 2]) ? 0 : (n - i)][i]) / factorial(i);
        }
        return result;
    }

    public static double stirlingInterpolation(double[] x, double[] y, double xCur) {
        boolean isEquallySpaced = true;
        double h = Math.round((x[1] - x[0]) * 1000) / 1000.0;
        int n = x.length;
        for (int i = 1; i < n - 1; i++) {
            if (Math.round((x[i + 1] - x[i]) * 1000) / 1000.0 != h) {
                isEquallySpaced = false;
                break;
            }
        }
        if (!isEquallySpaced) {
            return Double.NaN; // Узлы не являются равноотстоящими
        }
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++) {
            a[i][0] = y[i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                a[j][i] = a[j + 1][i - 1] - a[j][i - 1];
            }
        }
        int x0 = (n % 2 == 0) ? n / 2 - 1 : n / 2;
        double t = (xCur - x[x0]) / h;
        double result = a[x0][0];
        double compT1 = t;
        double compT2 = t * t;
        int prNumber = 0;
        for (int i = 1; i < n; i++) {
            if (i % 2 == 0) {
                result += (compT2 / factorial(i)) * a[x0 - (i / 2)][i];
                compT2 *= (t * t - prNumber * prNumber);
            } else {
                result += (compT1 / factorial(i)) * ((a[x0 - ((i + 1) / 2)][i] + a[x0 - (((i + 1) / 2) - 1)][i]) / 2);
                prNumber++;
                compT1 *= (t * t - prNumber * prNumber);
            }
        }
        return result;
    }

    public static double besselInterpolation(double[] x, double[] y, double xCur) {
        boolean isEquallySpaced = true;
        double h = Math.round((x[1] - x[0]) * 1000) / 1000.0;
        int n = x.length;
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++) {
            a[i][0] = y[i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                a[j][i] = a[j + 1][i - 1] - a[j][i - 1];
            }
        }
        for (int i = 1; i < n - 1; i++) {
            if (Math.round((x[i + 1] - x[i]) * 1000) / 1000.0 != h) {
                isEquallySpaced = false;
                break;
            }
        }
        if (!isEquallySpaced) {
            return Double.NaN; // Узлы не являются равноотстоящими
        }
        int x0 = (n % 2 == 0) ? n / 2 - 1 : n / 2;
        double t = (xCur - x[x0]) / h;
        double result = (a[x0][0] + a[x0 + 1][0]) / 2;
        result += (t - 0.5) * a[x0][1];
        double compT = t;
        int lastNumber = 0;
        for (int i = 2; i < n; i++) {
            if (i % 2 == 0) {
                lastNumber++;
                compT *= (t - lastNumber);
                result += (compT / factorial(i)) * ((a[x0 - i / 2][i] + a[x0 - ((i / 2) - 1)][i]) / 2);
            } else {
                result += (compT * (t - 0.5) / factorial(i)) * a[x0 - ((i - 1) / 2)][i];
                compT *= (t + lastNumber);
            }
        }
        return result;
    }

    public static double tCalc(double t, int n, boolean forward) {
        double result = t;
        for (int i = 1; i < n; i++) {
            result *= forward ? (t - i) : (t + i);
        }
        return result;
    }

    public static int factorial(int n) {
        if (n == 0) return 1;
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}

class PlotFrame extends JFrame {
    private double[] x, y;
    private double answer2, answer3, answer4, answer5;

    public PlotFrame(double[] x, double[] y, double answer2, double answer3, double answer4, double answer5) {
        this.x = x;
        this.y = y;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;

        setTitle("Interpolation Plot");
        setSize(800, 600);
        add(new PlotPanel());
    }

    class PlotPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            double minX = Arrays.stream(x).min().getAsDouble();
            double maxX = Arrays.stream(x).max().getAsDouble();
            double minY = Arrays.stream(y).min().getAsDouble();
            double maxY = Arrays.stream(y).max().getAsDouble();

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            double xScale = panelWidth / (maxX - minX);
            double yScale = panelHeight / (maxY - minY);

            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            for (int i = 0; i < x.length; i++) {
                int x1 = (int) ((x[i] - minX) * xScale);
                int y1 = panelHeight - (int) ((y[i] - minY) * yScale);
                g2.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
            }

            // Plot Newton's polynomial
            g2.setColor(Color.GREEN);
            for (double i = minX; i <= maxX; i += (maxX - minX) / 100) {
                int x1 = (int) ((i - minX) * xScale);
                int y1 = panelHeight - (int) ((Interpolation.newtonPolynomial(x, y, i) - minY) * yScale);
                g2.draw(new Ellipse2D.Double(x1 - 1, y1 - 1, 2, 2));
            }

            // Plot other polynomials (if not equal spaced, they will return NaN)
            if (!Double.isNaN(answer3)) {
                g2.setColor(Color.RED);
                for (double i = minX; i <= maxX; i += (maxX - minX) / 100) {
                    int x1 = (int) ((i - minX) * xScale);
                    int y1 = panelHeight - (int) ((Interpolation.newtonInterpolation(x, y, i) - minY) * yScale);
                    g2.draw(new Ellipse2D.Double(x1 - 1, y1 - 1, 2, 2));
                }
            }

            if (!Double.isNaN(answer4)) {
                g2.setColor(Color.BLUE);
                for (double i = minX; i <= maxX; i += (maxX - minX) / 100) {
                    int x1 = (int) ((i - minX) * xScale);
                    int y1 = panelHeight - (int) ((Interpolation.stirlingInterpolation(x, y, i) - minY) * yScale);
                    g2.draw(new Ellipse2D.Double(x1 - 1, y1 - 1, 2, 2));
                }
            }

            if (!Double.isNaN(answer5)) {
                g2.setColor(Color.ORANGE);
                for (double i = minX; i <= maxX; i += (maxX - minX) / 100) {
                    int x1 = (int) ((i - minX) * xScale);
                    int y1 = panelHeight - (int) ((Interpolation.besselInterpolation(x, y, i) - minY) * yScale);
                    g2.draw(new Ellipse2D.Double(x1 - 1, y1 - 1, 2, 2));
                }
            }
        }
    }
}
