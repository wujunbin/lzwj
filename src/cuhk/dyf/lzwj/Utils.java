package cuhk.dyf.lzwj;

/**
 * Created by dongyufei on 3/17/15.
 */
public class Utils {
    public static int leastCommonMultiple(int a, int b) {
        return Math.abs((a * b) / greatestCommonDivisor(a, b));
    }

    public static int greatestCommonDivisor(int a, int b) {
        int tmp = 0;
        while (b != 0) {
            tmp = b;
            b = a % b;
            a = tmp;
        }
        return Math.abs(a);
    }


}
