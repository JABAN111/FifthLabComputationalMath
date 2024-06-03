package Utils;

import Computational.math.Utils.CalculatorTables;
import Computational.math.Utils.FunctionalTable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class CalculatorTablesTest {
    @Test
    public void gotTableAndFinite(){
        Function<Double,Double> f = x->x*x;
        double a = -5d;
        double b = 5d;
        var n = 10;
        FunctionalTable functionalTable = CalculatorTables.createTable(a,b,n,f);
        List<List<Double>> finiteDiff = new ArrayList<>();
        CalculatorTables.finiteDiff(finiteDiff, List.of(functionalTable.getyArr()));
        System.out.println(finiteDiff);
    }
}
