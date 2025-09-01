package com.waveraven.moveout.calculator;

import com.waveraven.moveout.calculator.entity.Bill;
import com.waveraven.moveout.calculator.entity.CalculationResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 账单服务类，处理退租费用计算的业务逻辑
 */
public class BillingService {

    private static final String WATER_CALCULATOR = "3.50";

    private static final String ELECTRICITY_CALCULATOR = "1.20";

    private static final String GAS_CALCULATOR = "2.80";

    /**
     * 运行费用计算流程
     */
    public void runCalculation() {
        // 创建示例账单
        Bill bill = createSampleBill();

        // 创建计算器并计算费用
        MoveOutCalculator calculator = new MoveOutCalculator(
                // 水费单价
            new BigDecimal(WATER_CALCULATOR),
                // 电费单价
            new BigDecimal(ELECTRICITY_CALCULATOR),
                // 燃气费单价
            new BigDecimal(GAS_CALCULATOR)
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
        // 入住时欠水费25.50元
        bill.setWaterReadingIn(new BigDecimal("-25.50"));
        // 入住时水电余额50元
        bill.setElectricityReadingIn(new BigDecimal("50.00"));
        // 入住时欠燃气费10元
        bill.setGasReadingIn(new BigDecimal("-10.00"));

        // 设置退租信息
        bill.setCheckOutDate(LocalDateTime.of(2023, 12, 31, 10, 0));
        // 退租时水表读数
        bill.setWaterReadingOut(new BigDecimal("150.00"));
        // 退租时电表读数
        bill.setElectricityReadingOut(new BigDecimal("80.00"));
        // 退租时燃气表读数
        bill.setGasReadingOut(new BigDecimal("200.00"));

        // 设置付费模式
        // 默认后付费模式
        bill.setPaymentMode("postpaid");

        return bill;
    }

    /**
     * 显示计算结果
     */
    private void displayResult(Bill bill, CalculationResult result) {
        // 创建日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

        System.out.println("=== 退租费用结算单 ===");
        System.out.printf("入住时间: %s\n", bill.getCheckInDate().format(formatter));
        System.out.printf("退租时间: %s\n", bill.getCheckOutDate().format(formatter));
        System.out.printf("付费模式: %s\n", "prepaid".equals(result.getPaymentMode()) ? "预付款模式" : "后付费模式");
        System.out.println("------------------------");

        System.out.println("入住时状态：");
        System.out.printf("  水费: %s 元 (负数为欠费)\n", bill.getWaterReadingIn());
        System.out.printf("  电费: %s 元 (负数为欠费)\n", bill.getElectricityReadingIn());
        System.out.printf("  燃气费: %s 元 (负数为欠费)\n", bill.getGasReadingIn());

        System.out.println("\n退租时读数：");
        System.out.printf("  水表: %s 元\n", bill.getWaterReadingOut());
        System.out.printf("  电表: %s 元\n", bill.getElectricityReadingOut());
        System.out.printf("  燃气表: %s 元\n", bill.getGasReadingOut());

        System.out.println("\n使用量明细：");
        System.out.printf("  水使用量: %s\n", result.getWaterConsumption());
        System.out.printf("  电使用量: %s\n", result.getElectricityConsumption());
        System.out.printf("  燃气使用量: %s\n", result.getGasConsumption());

        System.out.println("\n费用明细：");
        System.out.printf("  水费消耗: %s 元，费用: %s 元\n",
                result.getWaterConsumption(), result.getWaterCost());
        System.out.printf("  电费消耗: %s 元，费用: %s 元\n",
                result.getElectricityConsumption(), result.getElectricityCost());
        System.out.printf("  燃气费消耗: %s 元，费用: %s 元\n",
                result.getGasConsumption(), result.getGasCost());
        System.out.println("------------------------");
        System.out.printf("总计应付费用: %s 元\n", result.getTotalCost());

        // 如果是预付款模式，显示预付款和退款信息
        if ("prepaid".equals(result.getPaymentMode())) {
            System.out.printf("预付款金额: %s 元\n", result.getPrepaidAmount());
            if (result.getRefundAmount().compareTo(BigDecimal.ZERO) > 0) {
                System.out.printf("应退款给用户: %s 元\n", result.getRefundAmount());
            } else if (result.getRefundAmount().compareTo(BigDecimal.ZERO) < 0) {
                System.out.printf("用户需补缴: %s 元\n", result.getRefundAmount().abs());
            } else {
                System.out.println("预付款与实际费用一致，无需退款或补缴");
            }
        }
    }
}
