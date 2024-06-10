package Computational.math.Methods;

import Computational.math.Utils.FunctionalTable;

import static Computational.math.Utils.MathUtils.factorial;

public class NewtonMethodInterpolation extends AbstractMethod{
    public NewtonMethodInterpolation() {
        super("Метод Ньютона с конечными разностями");
    }

    @Override
    public Double apply(FunctionalTable functionalTable, double xCur) {
            var x = functionalTable.getxArr();
            var y = functionalTable.getyArr();
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

    private double tCalc(double t, int n, boolean forward) {
        double result = t;
        for (int i = 1; i < n; i++) {
            result *= forward ? (t - i) : (t + i);
        }
        return result;
    }

}
