package Computational.math.Methods;

public abstract class AbstractMethod {
    String name;
    public AbstractMethod(String name){
        this.name = name;
    }
    public abstract Double apply(Double[] xArr,Double[] yArr, double x_current);
}
