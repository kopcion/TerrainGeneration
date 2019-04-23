package generators;

public class Utils {


    static float pow(float x, int n) {
        if(n == 0) {
            return 1;
        }
        return x * pow(x, n-1);
    }

    static float nthRoot(int n, float number) {
        return (float)Math.pow(Math.E, Math.log(number)/n);
    }
}
