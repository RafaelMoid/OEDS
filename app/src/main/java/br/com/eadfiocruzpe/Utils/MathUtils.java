package br.com.eadfiocruzpe.Utils;

import java.util.Random;

public class MathUtils {

    public int getRandomNumber(int start, int end) {
        Random random = new Random();

        if (start > end) {
            return -1;

        } else {
            // Get the range, casting to long to avoid overflow problems
            long range = (long)end - (long)start + 1;

            // Compute a fraction of the range, 0 <= fraction < range
            long fraction = (long)(range * random.nextDouble());

            return (int)(fraction + start);
        }
    }

}