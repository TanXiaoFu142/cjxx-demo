package com.xf.util;

/**
 * @author 谭俊杰
 * @date 2022/8/19
 * @time 16:17
 */
public class Payment {

    public Payment(Double paymentAmount, Double totalPayment) {
        PaymentAmount = paymentAmount;
        this.totalPayment = totalPayment;
    }

    private Double PaymentAmount;
    private Double totalPayment;

    public Double getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        PaymentAmount = paymentAmount;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "PaymentAmount=" + PaymentAmount +
                ", totalPayment=" + totalPayment +
                '}';
    }
}
