package com.waveraven.moveout.calculator;

import com.waveraven.moveout.calculator.entity.Bill;
import com.waveraven.moveout.calculator.entity.CalculationResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 退租费用计算器
 */
public class MoveOutCalculator {
    private final BigDecimal waterRate;       // 水费单价
    private final BigDecimal electricityRate; // 电费单价
    private final BigDecimal gasRate;         // 燃气费单价

    public MoveOutCalculator(BigDecimal waterRate, BigDecimal electricityRate, BigDecimal gasRate) {
        this.waterRate = waterRate;
        this.electricityRate = electricityRate;
        this.gasRate = gasRate;
    }

    /**
     * 根据账单计算费用
     *
     * @param bill 账单信息
     * @return 计算结果
     */
    public CalculationResult calculate(Bill bill) {
        // 计算实际消耗量（退租读数 - 入住读数）
        // 入住读数为负表示欠费，为正表示余额
        BigDecimal waterConsumption = bill.getWaterReadingOut()
                .subtract(bill.getWaterReadingIn());

        BigDecimal electricityConsumption = bill.getElectricityReadingOut()
                .subtract(bill.getElectricityReadingIn());

        BigDecimal gasConsumption = bill.getGasReadingOut()
                .subtract(bill.getGasReadingIn());

        // 计算各项费用（保留2位小数）
        BigDecimal waterCost = waterConsumption.multiply(waterRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal electricityCost = electricityConsumption.multiply(electricityRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal gasCost = gasConsumption.multiply(gasRate)
                .setScale(2, RoundingMode.HALF_UP);

        // 计算总费用
        BigDecimal totalCost = waterCost.add(electricityCost).add(gasCost)
                .setScale(2, RoundingMode.HALF_UP);

        return new CalculationResult(waterConsumption, electricityConsumption, gasConsumption,
                waterCost, electricityCost, gasCost, totalCost);
    }
}
