public class QuadraticEquation {
    public double[] findRoots(double a, double b, double c) {
        double delta=b*b-4*a*c;
        if(delta==0) {
            double p1=-b/(2*a);
            return new double[]{p1};
        }
        else if(delta>0){
            double p1=(-b-Math.sqrt(delta))/(2*a), p2=(-b+Math.sqrt(delta))/(2*a);
            return new double[]{p1, p2};
        }
        return new double[0];
    }
}
