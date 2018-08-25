package metaextract.nkm.com.myplayer;

import java.util.Vector;

public class ArithmeticFunctions {

    public double Sum(Vector<Double> vec){
        double res= 0;
        if(vec == null) return 0;
        for (double val :
                vec) {
            res+=val;
        }
        return res;
    }

    public double SD(Vector<Double> vec){
        if(vec == null) return 0;
        if(vec.size() > 0) {
            double x = 0; //(xi-x_)
            double sum = Sum(vec);
            double avg = (sum / vec.size());
            for (double val :
                    vec) {
                x += Math.pow((val - avg), 2);
            }
            double w = ((double)1/vec.size());
            double y = x * w;
            return Math.sqrt(y);
        }
        else return 0;
    }

    public double Max(Vector<Double> vec){
        if(vec == null) return 0;
        double max = vec.elementAt(0);
        for (double d :
                vec) {
            if (d > max) max = d;
        }
        return max;
    }

    private double Min(Vector<Double> vec) {
        if(vec == null) return 0;
        double min = vec.elementAt(0);
        for (double d :
                vec) {
            if (d < min) min = d;
        }
        return min;
    }

    public MyLambdaFunction getAvg() {
        MyLambdaFunction Avg = (vec) -> {
            if(vec == null) return 0;
            double sum = Sum(vec);
            if(vec.size() != 0){
                return sum/vec.size();
            }
            else return (0);
        };
        return Avg;
    }

    public MyLambdaFunction getSD(){
        MyLambdaFunction Sd = (vec) -> {
            double sd = SD(vec);
            return sd;
        };
        return Sd;
    }

    MyLambdaFunction getMax(){
        MyLambdaFunction max = (vec) -> {
            double res = Max(vec);
            return res;
        };
        return max;
    }

    MyLambdaFunction getMin(){
        MyLambdaFunction min = (vec) -> {
           double res = Min(vec);
          return res;
        };
        return min;
    }

}