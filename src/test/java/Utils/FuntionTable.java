package Utils;

import Computational.math.Utils.FunctionalTable;
import org.junit.Test;

public class FuntionTable {
    @Test
    public void testPrint(){
        Double[] xArr = new Double[] {
                0.1,0.2,0.3,0.4,0.5
        };
        Double[] yArr = new Double[]{
                1.25,2.38,3.79,5.44,7.14
        };
        FunctionalTable f = new FunctionalTable(xArr,yArr);
        f.printTable();
    }
}
