package com.waveraven.moveout.calculator.service;

import com.waveraven.moveout.calculator.entity.Bill;
import com.waveraven.moveout.calculator.entity.CalculationResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

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
        // 创建账单（通过用户输入）
        Bill bill = createBillFromInput();

        // 创建计算器并计算费用
        MoveOutCalculatorService calculator = new MoveOutCalculatorService(
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
     * 通过用户输入创建账单
     */
    private Bill createBillFromInput() {
        Bill bill = new Bill();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 请输入账单信息 ===");

        // 输入入住信息
        System.out.println("入住信息：");
        System.out.print("请输入入住年份: ");
        int checkInYear = scanner.nextInt();
        System.out.print("请输入入住月份: ");
        int checkInMonth = scanner.nextInt();
        System.out.print("请输入入住日期: ");
        int checkInDay = scanner.nextInt();

        // 设置默认时间为当天0点0分
        bill.setCheckInDate(LocalDateTime.of(checkInYear, checkInMonth, checkInDay, 0, 0));

        System.out.print("请输入入住时水表金额(正数表示余额，负数表示欠费): ");
        bill.setWaterReadingIn(new BigDecimal(scanner.next()));
        System.out.print("请输入入住时电表金额(正数表示余额，负数表示欠费): ");
        bill.setElectricityReadingIn(new BigDecimal(scanner.next()));
        System.out.print("请输入入住时燃气表金额(正数表示余额，负数表示欠费): ");
        bill.setGasReadingIn(new BigDecimal(scanner.next()));

        // 输入退租信息
        System.out.println("\n退租信息：");
        System.out.print("请输入退租年份: ");
        int checkOutYear = scanner.nextInt();
        System.out.print("请输入退租月份: ");
        int checkOutMonth = scanner.nextInt();
        System.out.print("请输入退租日期: ");
        int checkOutDay = scanner.nextInt();

        // 设置默认时间为当天0点0分
        bill.setCheckOutDate(LocalDateTime.of(checkOutYear, checkOutMonth, checkOutDay, 0, 0));

        System.out.print("请输入退租时水表金额(正数表示余额，负数表示欠费): ");
        bill.setWaterReadingOut(new BigDecimal(scanner.next()));
        System.out.print("请输入退租时电表金额(正数表示余额，负数表示欠费): ");
        bill.setElectricityReadingOut(new BigDecimal(scanner.next()));
        System.out.print("请输入退租时燃气表金额(正数表示余额，负数表示欠费): ");
        bill.setGasReadingOut(new BigDecimal(scanner.next()));

        return bill;
    }

    /**
     * 显示计算结果
     */
    private void displayResult(Bill bill, CalculationResult result) {
        // 创建日期时间格式化器（只显示年月日）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

        System.out.println("\n=== 退租费用结算单 ===");
        System.out.printf("入住时间: %s\n", bill.getCheckInDate().format(formatter));
        System.out.printf("退租时间: %s\n", bill.getCheckOutDate().format(formatter));
        System.out.printf("居住天数: %d 天\n",
            java.time.temporal.ChronoUnit.DAYS.between(bill.getCheckInDate(), bill.getCheckOutDate()));
        System.out.println("------------------------");

        System.out.println("入住时状态：");
        System.out.printf("  水费: %s 元 (%s)\n", bill.getWaterReadingIn(),
                bill.getWaterReadingIn().compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");
        System.out.printf("  电费: %s 元 (%s)\n", bill.getElectricityReadingIn(),
                bill.getElectricityReadingIn().compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");
        System.out.printf("  燃气费: %s 元 (%s)\n", bill.getGasReadingIn(),
                bill.getGasReadingIn().compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");

        BigDecimal totalIn = bill.getWaterReadingIn()
                .add(bill.getElectricityReadingIn())
                .add(bill.getGasReadingIn());
        System.out.printf("  总计: %s 元 (%s)\n", totalIn,
                totalIn.compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");

        System.out.println("\n退租时状态：");
        System.out.printf("  水表: %s 元 (%s)\n", bill.getWaterReadingOut(),
                bill.getWaterReadingOut().compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");
        System.out.printf("  电表: %s 元 (%s)\n", bill.getElectricityReadingOut(),
                bill.getElectricityReadingOut().compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");
        System.out.printf("  燃气表: %s 元 (%s)\n", bill.getGasReadingOut(),
                bill.getGasReadingOut().compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");

        BigDecimal totalOut = bill.getWaterReadingOut()
                .add(bill.getElectricityReadingOut())
                .add(bill.getGasReadingOut());
        System.out.printf("  总计: %s 元 (%s)\n", totalOut,
                totalOut.compareTo(BigDecimal.ZERO) >= 0 ? "余额" : "欠费");

        System.out.println("\n账户变动明细：");
        System.out.printf("  水费变动: %s 元 (%s)\n", result.getWaterConsumption(),
                result.getWaterConsumption().compareTo(BigDecimal.ZERO) >= 0 ? "充值/余额增加" : "消费/余额减少");
        System.out.printf("  电费变动: %s 元 (%s)\n", result.getElectricityConsumption(),
                result.getElectricityConsumption().compareTo(BigDecimal.ZERO) >= 0 ? "充值/余额增加" : "消费/余额减少");
        System.out.printf("  燃气费变动: %s 元 (%s)\n", result.getGasConsumption(),
                result.getGasConsumption().compareTo(BigDecimal.ZERO) >= 0 ? "充值/余额增加" : "消费/余额减少");

        System.out.println("\n费用明细：");
        System.out.printf("  水费: %s 元\n", result.getWaterCost());
        System.out.printf("  电费: %s 元\n", result.getElectricityCost());
        System.out.printf("  燃气费: %s 元\n", result.getGasCost());
        System.out.println("------------------------");
        System.out.printf("总计变动金额: %s 元\n", result.getTotalCost());

        // 显示退款信息
        // 退款金额 = 入住时总余额 - 退租时总余额
        // 正数表示入住时余额多，退租时余额少，用户消费了，需要补缴给房东
        // 负数表示入住时余额少，退租时余额多，用户充值了，需要房东退款给用户
        if (result.getRefundAmount().compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf("用户需补缴给房东: %s 元\n", result.getRefundAmount());
        } else if (result.getRefundAmount().compareTo(BigDecimal.ZERO) < 0) {
            System.out.printf("房东需退款给用户: %s 元\n", result.getRefundAmount().abs());
        } else {
            System.out.println("账户余额无变动，无需退款或补缴");
        }
    }
}
