package com.waveraven.moveout.calculator;

import com.waveraven.moveout.calculator.entity.Bill;
import com.waveraven.moveout.calculator.entity.CalculationResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单服务类，处理退租费用计算的业务逻辑
 */
public class BillingService {

    /**
     * 运行费用计算流程
     */
    public void runCalculation() {
        // 创建示例账单
        Bill bill = createSampleBill();

        // 创建计算器并计算费用
        MoveOutCalculator calculator = new MoveOutCalculator(
            new BigDecimal("3.50"),   // 水费单价
            new BigDecimal("1.20"),   // 电费单价
            new BigDecimal("2.80")    // 燃气费单价
        );

        // 执行计算
        CalculationResult result = calculator.calculate(bill);

        // 显示结果
        displayResult(bill, result);
    }

    /**
     * 创建示例账单
     */
    private Bill createSampleBill() {
        Bill bill = new Bill();

        // 设置入住信息
        bill.setCheckInDate(LocalDateTime.of(2023, 1, 1, 14, 0));
        bill.setWaterReadingIn(new BigDecimal("-25.50"));     // 入住时欠水费25.50元
        bill.setElectricityReadingIn(new BigDecimal("50.00")); // 入住时水电余额50元
        bill.setGasReadingIn(new BigDecimal("-10.00"));       // 入住时欠燃气费10元

        // 设置退租信息
        bill.setCheckOutDate(LocalDateTime.of(2023, 12, 31, 10, 0));
        bill.setWaterReadingOut(new BigDecimal("150.00"));    // 退租时水表读数
        bill.setElectricityReadingOut(new BigDecimal("80.00")); // 退租时电表读数
        bill.setGasReadingOut(new BigDecimal("200.00"));      // 退租时燃气表读数

        return bill;
    }

    /**
     * 显示计算结果
     */
    private void displayResult(Bill bill, CalculationResult result) {
        System.out.println("=== 退租费用结算单 ===");
        System.out.printf("入住时间: %s\n", bill.getCheckInDate());
        System.out.printf("退租时间: %s\n", bill.getCheckOutDate());
        System.out.println("------------------------");
        System.out.println("入住时状态：");
        System.out.printf("  水费: %s 元 (负数为欠费)\n", bill.getWaterReadingIn());
        System.out.printf("  电费: %s 元 (负数为欠费)\n", bill.getElectricityReadingIn());
        System.out.printf("  燃气费: %s 元 (负数为欠费)\n", bill.getGasReadingIn());
        System.out.println("\n退租时读数：");
        System.out.printf("  水表: %s 元\n", bill.getWaterReadingOut());
        System.out.printf("  电表: %s 元\n", bill.getElectricityReadingOut());
        System.out.printf("  燃气表: %s 元\n", bill.getGasReadingOut());
        System.out.println("\n费用明细：");
        System.out.printf("  水费消耗: %s 元，费用: %s 元\n",
                         result.getWaterConsumption(), result.getWaterCost());
        System.out.printf("  电费消耗: %s 元，费用: %s 元\n",
                         result.getElectricityConsumption(), result.getElectricityCost());
        System.out.printf("  燃气费消耗: %s 元，费用: %s 元\n",
                         result.getGasConsumption(), result.getGasCost());
        System.out.println("------------------------");
        System.out.printf("总计应付费用: %s 元\n", result.getTotalCost());
    }
}
