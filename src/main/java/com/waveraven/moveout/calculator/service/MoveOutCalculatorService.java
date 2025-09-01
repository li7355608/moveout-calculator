package com.waveraven.moveout.calculator.service;

import com.waveraven.moveout.calculator.entity.Bill;
import com.waveraven.moveout.calculator.entity.CalculationResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 退租费用计算器
 */
public class MoveOutCalculatorService {
    private final BigDecimal waterRate;       // 水费单价
    private final BigDecimal electricityRate; // 电费单价
    private final BigDecimal gasRate;         // 燃气费单价

    public MoveOutCalculatorService(BigDecimal waterRate, BigDecimal electricityRate, BigDecimal gasRate) {
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
        // 计算消耗金额（退租读数 - 入住读数）
        // 正数表示充值/余额增加，负数表示消耗/余额减少
        BigDecimal waterAmountConsumed = bill.getWaterReadingOut()
                .subtract(bill.getWaterReadingIn());

        BigDecimal electricityAmountConsumed = bill.getElectricityReadingOut()
                .subtract(bill.getElectricityReadingIn());

        BigDecimal gasAmountConsumed = bill.getGasReadingOut()
                .subtract(bill.getGasReadingIn());

        // 计算各项费用（消耗金额的绝对值，保留2位小数）
        BigDecimal waterCost = waterAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);
        BigDecimal electricityCost = electricityAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);
        BigDecimal gasCost = gasAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);

        // 计算总费用
        BigDecimal totalCost = waterCost.add(electricityCost).add(gasCost)
                .setScale(2, RoundingMode.HALF_UP);

        // 计算退款金额（正数表示退给用户，负数表示用户需要支付）
        // 退款金额 = 入住时总余额 - 退租时总余额
        // 如果入住时余额多，退租时余额少，说明消费了，用户需要补缴（负数）
        // 如果入住时余额少，退租时余额多，说明充值了，需要退款给用户（正数）
        BigDecimal totalIn = bill.getWaterReadingIn()
                .add(bill.getElectricityReadingIn())
                .add(bill.getGasReadingIn());

        BigDecimal totalOut = bill.getWaterReadingOut()
                .add(bill.getElectricityReadingOut())
                .add(bill.getGasReadingOut());

        BigDecimal refundAmount = totalIn.subtract(totalOut);

        return new CalculationResult(waterAmountConsumed, electricityAmountConsumed, gasAmountConsumed,
                waterCost, electricityCost, gasCost, totalCost, BigDecimal.ZERO,
                refundAmount, "simple");
    }
}
