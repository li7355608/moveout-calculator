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

        // 计算实际使用量（只有当消耗金额为负数时才表示实际使用）
        // 如果消耗金额为正，表示充值，使用量为0
        BigDecimal waterUsage = BigDecimal.ZERO;
        BigDecimal electricityUsage = BigDecimal.ZERO;
        BigDecimal gasUsage = BigDecimal.ZERO;

        if (waterAmountConsumed.compareTo(BigDecimal.ZERO) < 0) {
            waterUsage = waterAmountConsumed.abs().divide(waterRate, 2, RoundingMode.HALF_UP);
        }

        if (electricityAmountConsumed.compareTo(BigDecimal.ZERO) < 0) {
            electricityUsage = electricityAmountConsumed.abs().divide(electricityRate, 2, RoundingMode.HALF_UP);
        }

        if (gasAmountConsumed.compareTo(BigDecimal.ZERO) < 0) {
            gasUsage = gasAmountConsumed.abs().divide(gasRate, 2, RoundingMode.HALF_UP);
        }

        // 计算各项费用（只有消耗才需要付费，充值不收费）
        BigDecimal waterCost = BigDecimal.ZERO;
        BigDecimal electricityCost = BigDecimal.ZERO;
        BigDecimal gasCost = BigDecimal.ZERO;

        if (waterAmountConsumed.compareTo(BigDecimal.ZERO) < 0) {
            waterCost = waterAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);
        }

        if (electricityAmountConsumed.compareTo(BigDecimal.ZERO) < 0) {
            electricityCost = electricityAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);
        }

        if (gasAmountConsumed.compareTo(BigDecimal.ZERO) < 0) {
            gasCost = gasAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);
        }

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

        return new CalculationResult(waterUsage, electricityUsage, gasUsage,
                waterCost, electricityCost, gasCost, totalCost, prepaidAmount,
                refundAmount, paymentMode);
    }
}
