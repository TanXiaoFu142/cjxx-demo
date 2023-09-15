package com.xf;

import java.math.BigDecimal;
 public class PiCalculator implements Runnable {
    private static final int SCALE = 1000000; // Precision
    private static final BigDecimal FOUR = BigDecimal.valueOf(4);
     private int digits;
     public PiCalculator(int digits) {
        this.digits = digits;
    }
     @Override
    public void run() {
        BigDecimal pi = computePi(digits);
        System.out.println("Calculation complete! The value of pi is: " + pi);
    }
     private BigDecimal computePi(int digits) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < digits; i++) {
            BigDecimal term = BigDecimal.ONE.divide(BigDecimal.valueOf(2 * i + 1), SCALE, BigDecimal.ROUND_HALF_EVEN);
            if (i % 2 == 0) {
                sum = sum.add(term);
            } else {
                sum = sum.subtract(term);
            }
        }
        return sum.multiply(FOUR);
    }
     public static void main(String[] args) {
        int digits = 1000; // Number of digits to calculate pi
        Thread piThread = new Thread(new PiCalculator(digits));
        piThread.start();
    }
}
