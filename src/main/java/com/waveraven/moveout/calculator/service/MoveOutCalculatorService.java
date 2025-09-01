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
        // 计算实际消耗量
        // 公式：消耗量 = 退租读数 - 入住读数
        // 解释：
        // 1. 如果入住为正数(余额)，退租为负数(欠费)：
        //    消耗量 = 负数 - 正数 = 更大的负数，表示消耗了更多
        // 2. 如果入住为负数(欠费)，退租为正数(余额)：
        //    消耗量 = 正数 - 负数 = 更大的正数，表示充值了更多
        BigDecimal waterConsumption = bill.getWaterReadingOut()
                .subtract(bill.getWaterReadingIn());

        BigDecimal electricityConsumption = bill.getElectricityReadingOut()
                .subtract(bill.getElectricityReadingIn());

        BigDecimal gasConsumption = bill.getGasReadingOut()
                .subtract(bill.getGasReadingIn());

        // 计算各项费用（保留2位小数）
        // 注意：消耗量为正表示用户充值/余额增加，为负表示实际消耗
        // 费用始终为正数，表示用户需要支付的金额
        BigDecimal waterCost = waterConsumption.multiply(waterRate)
                .abs() // 取绝对值确保费用为正数
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal electricityCost = electricityConsumption.multiply(electricityRate)
                .abs() // 取绝对值确保费用为正数
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal gasCost = gasConsumption.multiply(gasRate)
                .abs() // 取绝对值确保费用为正数
                .setScale(2, RoundingMode.HALF_UP);

        // 计算总费用
        BigDecimal totalCost = waterCost.add(electricityCost).add(gasCost)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal refundAmount = BigDecimal.ZERO;
        BigDecimal prepaidAmount = BigDecimal.ZERO;
        String paymentMode = bill.getPaymentMode();

        // 如果是预付款模式，计算退款金额
        if ("prepaid".equals(paymentMode)) {
            prepaidAmount = bill.getPrepaidAmount();
            refundAmount = prepaidAmount.subtract(totalCost);
        }

        return new CalculationResult(waterConsumption, electricityConsumption, gasConsumption,
                waterCost, electricityCost, gasCost, totalCost, prepaidAmount,
                refundAmount, paymentMode);
    }
}
