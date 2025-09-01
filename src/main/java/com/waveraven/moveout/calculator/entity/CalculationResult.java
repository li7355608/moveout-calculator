package com.waveraven.moveout.calculator.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 计算结果类
 */
@Data
public class CalculationResult {
    // 水消耗量
    private final BigDecimal waterConsumption;
    // 电消耗量
    private final BigDecimal electricityConsumption;
    // 燃气消耗量
    private final BigDecimal gasConsumption;

    // 水费
    private final BigDecimal waterCost;
    // 电费
    private final BigDecimal electricityCost;
    // 燃气费
    private final BigDecimal gasCost;

    // 总费用
    private final BigDecimal totalCost;

    // 预付款金额
    private final BigDecimal prepaidAmount;
    // 退款金额（正数表示退给用户，负数表示用户需要补缴）
    private final BigDecimal refundAmount;
    // 付费模式
    private final String paymentMode;

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
}
