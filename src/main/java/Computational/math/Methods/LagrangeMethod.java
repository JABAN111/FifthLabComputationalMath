package Computational.math.Methods;

public class LagrangeMethod extends AbstractMethod{
    public LagrangeMethod() {
        super("Полином Лагранжа");
    }

    @Override
    public Double apply(Double[] xArr, Double[] yArr, double x_current) {
        double res = 0d;
        for (int i = 0; i < xArr.length; i++) {
            var p = 1d;
            for (int j = 0; j < yArr.length; j++) {
                if (i != j){
                    p*=(x_current - xArr[j])/(xArr[i]-xArr[j]);
                }
            }
            res += p*yArr[i];
        }
        return res;
    }
}
