package Computational.math.Utils;

import Computational.math.Methods.*;

import java.util.ArrayList;
import java.util.List;

public class FabricMethods {
    private ArrayList<AbstractMethod> methodList;
    public FabricMethods(){
        methodList = new ArrayList<>(List.of(
                new LagrangeMethod(),
                new BesselInterpolation(),
                new NewtonMethodPolynomial(),
                new NewtonMethodInterpolation(),
                new StirlingInterpolation()
        ));
    }
    public void executeEverything(FunctionalTable functionalTable, double xCurr){
        for(AbstractMethod m : methodList){
            System.out.println(STR."\{m.getName()} дал ответ: \{m.apply(functionalTable, xCurr)}");
        }
    }
}
