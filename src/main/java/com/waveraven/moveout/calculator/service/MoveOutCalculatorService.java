package com.waveraven.moveout.calculator.service;

import com.waveraven.moveout.calculator.entity.Bill;
import com.waveraven.moveout.calculator.entity.CalculationResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 退租费用计算器
 */
public class MoveOutCalculatorService {
    // 水费单价
    private final BigDecimal waterRate;
    // 电费单价
    private final BigDecimal electricityRate;
    // 燃气费单价
    private final BigDecimal gasRate;

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
        // 计算理论余额（入住余额 + 充值金额）
        BigDecimal waterTheoreticalBalance = bill.getWaterReadingIn()
                .add(bill.getWaterRecharge());

        BigDecimal electricityTheoreticalBalance = bill.getElectricityReadingIn()
                .add(bill.getElectricityRecharge());

        BigDecimal gasTheoreticalBalance = bill.getGasReadingIn()
                .add(bill.getGasRecharge());

        // 计算实际消耗金额（理论余额 - 退租读数）
        // 这才是真正消耗掉的金额
        BigDecimal waterAmountConsumed = waterTheoreticalBalance
                .subtract(bill.getWaterReadingOut());

        BigDecimal electricityAmountConsumed = electricityTheoreticalBalance
                .subtract(bill.getElectricityReadingOut());

        BigDecimal gasAmountConsumed = gasTheoreticalBalance
                .subtract(bill.getGasReadingOut());

        // 计算实际使用量（消耗金额的绝对值 / 单价）
        // 使用量始终为正数
        BigDecimal waterUsage = waterAmountConsumed.abs().divide(waterRate, 2, RoundingMode.HALF_UP);
        BigDecimal electricityUsage = electricityAmountConsumed.abs().divide(electricityRate, 2, RoundingMode.HALF_UP);
        BigDecimal gasUsage = gasAmountConsumed.abs().divide(gasRate, 2, RoundingMode.HALF_UP);

        // 计算各项费用（消耗金额的绝对值）
        BigDecimal waterCost = waterAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);
        BigDecimal electricityCost = electricityAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);
        BigDecimal gasCost = gasAmountConsumed.abs().setScale(2, RoundingMode.HALF_UP);

        // 计算总费用
        BigDecimal totalCost = waterCost.add(electricityCost).add(gasCost)
                .setScale(2, RoundingMode.HALF_UP);

        // 计算退款金额（总充值金额 - 总消费金额）
        BigDecimal totalRecharge = bill.getWaterRecharge()
                .add(bill.getElectricityRecharge())
                .add(bill.getGasRecharge());

        BigDecimal refundAmount = totalRecharge.subtract(totalCost);

        return new CalculationResult(waterUsage, electricityUsage, gasUsage,
                waterCost, electricityCost, gasCost, totalCost, totalRecharge,
                refundAmount, "simple");
    }
}
