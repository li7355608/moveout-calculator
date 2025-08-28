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

    public CalculationResult(BigDecimal waterConsumption, BigDecimal electricityConsumption,
                           BigDecimal gasConsumption, BigDecimal waterCost,
                           BigDecimal electricityCost, BigDecimal gasCost,
                           BigDecimal totalCost) {
        this.waterConsumption = waterConsumption;
        this.electricityConsumption = electricityConsumption;
        this.gasConsumption = gasConsumption;
        this.waterCost = waterCost;
        this.electricityCost = electricityCost;
        this.gasCost = gasCost;
        this.totalCost = totalCost;
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
}
