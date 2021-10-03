package utils;

import java.util.LinkedList;

public class GeneratePrime {
    private static int[] primes;

    public static int[] singleThread(int len) {
        if (len <= 0 || len > 10000000)
            len = 10000000;   //最多算一千万个，占用40MB内存，第一千万个素数为   177,525,769

        primes = new int[len];
        primes[0] = 2;
        primes[1] = 3;
        int tempPrime = 3;
        for (int i=2; i<len; i++) {
            tempPrime = (int) getNextPrime(tempPrime + 2);
            primes[i] = tempPrime;
        }
        return primes;
    }

    private static long getNextPrime(long candidate) {   //candidate >= 5 且为奇数
        int endIndex = 0;
        long temp = primes[endIndex];
        while (temp*temp <= candidate) {
            endIndex++;
            temp = primes[endIndex];
        }

        for (int j=0; j<endIndex; j++)
            if (candidate % primes[j] == 0) {
                j = 0;
                candidate += 2;
            }
        return candidate;
    }
}
