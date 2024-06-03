package methods;

import Computational.math.Methods.LagrangeMethod;
import Computational.math.Utils.FunctionalTable;
import org.junit.Test;

import java.util.function.Function;

public class LagrangeMethodTest {
    @Test
    public void caseOne(){
        Function<Double,Double> f = Math::sin;
        Double[] xCurr = new Double[]{
                1d, 1.8,2.6,3.4,4.2
        };
        Double[] yCurr = new Double[xCurr.length];
        for (int i = 0; i < xCurr.length; i++) {
            yCurr[i] = f.apply(xCurr[i]);
        }
        LagrangeMethod l = new LagrangeMethod();
        System.out.println(l.apply(new FunctionalTable(xCurr,yCurr), 2.5));
    }
}
