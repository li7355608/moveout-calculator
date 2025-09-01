package com.waveraven.moveout.calculator.entity;

import java.math.BigDecimal;

/**
 * 计算结果类
 */
public class CalculationResult {
    private final BigDecimal waterConsumption;      // 水消耗量
    private final BigDecimal electricityConsumption; // 电消耗量
    private final BigDecimal gasConsumption;        // 燃气消耗量

    private final BigDecimal waterCost;             // 水费
    private final BigDecimal electricityCost;       // 电费
    private final BigDecimal gasCost;               // 燃气费
    private final BigDecimal totalCost;             // 总费用

    private final BigDecimal prepaidAmount;     // 预付款金额
    private final BigDecimal refundAmount;      // 退款金额（正数表示退给用户，负数表示用户需要补缴）
    private final String paymentMode;           // 付费模式

    public CalculationResult(BigDecimal waterConsumption, BigDecimal electricityConsumption,
                             BigDecimal gasConsumption, BigDecimal waterCost,
                             BigDecimal electricityCost, BigDecimal gasCost,
                             BigDecimal totalCost, BigDecimal prepaidAmount,
                             BigDecimal refundAmount, String paymentMode) {
        this.waterConsumption = waterConsumption;
        this.electricityConsumption = electricityConsumption;
        this.gasConsumption = gasConsumption;
        this.waterCost = waterCost;
        this.electricityCost = electricityCost;
        this.gasCost = gasCost;
        this.totalCost = totalCost;
        this.prepaidAmount = prepaidAmount;
        this.refundAmount = refundAmount;
        this.paymentMode = paymentMode;
    }

    // Getters
    public BigDecimal getWaterConsumption() {
        return waterConsumption;
    }

    public BigDecimal getElectricityConsumption() {
        return electricityConsumption;
    }

    public BigDecimal getGasConsumption() {
        return gasConsumption;
    }

    public BigDecimal getWaterCost() {
        return waterCost;
    }

    public BigDecimal getElectricityCost() {
        return electricityCost;
    }

    public BigDecimal getGasCost() {
        return gasCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal getPrepaidAmount() {
        return prepaidAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }
}
