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

    static void print(Object[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                System.out.print(values[i][j] + " ");
            }
            System.out.print('\n');
        }
    }
}
