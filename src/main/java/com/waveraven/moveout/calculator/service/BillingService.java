package com.waveraven.moveout.calculator.service;

import com.waveraven.moveout.calculator.entity.Bill;
import com.waveraven.moveout.calculator.entity.CalculationResult;
import com.waveraven.moveout.calculator.utils.ConsoleColors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * 账单服务类，处理退租费用计算的业务逻辑
 */
public class BillingService {

    // 默认阶梯水费单价
    private static final String WATER_CALCULATOR = "3.1";
    // 默认阶梯电费单价
    private static final String ELECTRICITY_CALCULATOR = "0.56";
    // 默认阶梯燃气费单价
    private static final String GAS_CALCULATOR = "2.94";

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

        System.out.println(ConsoleColors.CYAN_BOLD + "=== 退租计算账单信息 ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "------------------------" + ConsoleColors.RESET);

        // 输入入住信息
        System.out.println(ConsoleColors.YELLOW + "住户入住信息：" + ConsoleColors.RESET);
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

        // 输入充值信息
        System.out.println("\n" + ConsoleColors.YELLOW + "住户总充值信息：" + ConsoleColors.RESET);
        System.out.print("请输入水费充值金额: ");
        bill.setWaterRecharge(new BigDecimal(scanner.next()));
        System.out.print("请输入电费充值金额: ");
        bill.setElectricityRecharge(new BigDecimal(scanner.next()));
        System.out.print("请输入燃气费充值金额: ");
        bill.setGasRecharge(new BigDecimal(scanner.next()));

        // 输入退租信息
        System.out.println("\n" + ConsoleColors.YELLOW + "住户退租信息：" + ConsoleColors.RESET);
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

        System.out.println(ConsoleColors.PURPLE + "------------------------" + ConsoleColors.RESET);
        return bill;
    }

    /**
     * 显示计算结果
     */
    private void displayResult(Bill bill, CalculationResult result) {
        // 创建日期时间格式化器（只显示年月日）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

        System.out.println("\n" + ConsoleColors.GREEN_BOLD + "=== 退租费用结算单 ===" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.CYAN + "入住时间: " + ConsoleColors.RESET + "%s\n", bill.getCheckInDate().format(formatter));
        System.out.printf(ConsoleColors.CYAN + "退租时间: " + ConsoleColors.RESET + "%s\n", bill.getCheckOutDate().format(formatter));
        System.out.printf(ConsoleColors.CYAN + "居住天数: " + ConsoleColors.RESET + "%d 天\n",
            java.time.temporal.ChronoUnit.DAYS.between(bill.getCheckInDate(), bill.getCheckOutDate()));
        System.out.println(ConsoleColors.PURPLE + "------------------------" + ConsoleColors.RESET);

        System.out.println(ConsoleColors.YELLOW + "住户入住时状态：" + ConsoleColors.RESET);
        System.out.printf("  水费: %s 元 (%s)\n",
            bill.getWaterReadingIn().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + bill.getWaterReadingIn() + ConsoleColors.RESET :
                ConsoleColors.RED + bill.getWaterReadingIn() + ConsoleColors.RESET,
            bill.getWaterReadingIn().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.printf("  电费: %s 元 (%s)\n",
            bill.getElectricityReadingIn().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + bill.getElectricityReadingIn() + ConsoleColors.RESET :
                ConsoleColors.RED + bill.getElectricityReadingIn() + ConsoleColors.RESET,
            bill.getElectricityReadingIn().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.printf("  燃气费: %s 元 (%s)\n",
            bill.getGasReadingIn().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + bill.getGasReadingIn() + ConsoleColors.RESET :
                ConsoleColors.RED + bill.getGasReadingIn() + ConsoleColors.RESET,
            bill.getGasReadingIn().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.println("\n" + ConsoleColors.YELLOW + "住户充值金额：" + ConsoleColors.RESET);
        System.out.printf("  水费充值: %s 元\n", ConsoleColors.BLUE + bill.getWaterRecharge().toString() + ConsoleColors.RESET);
        System.out.printf("  电费充值: %s 元\n", ConsoleColors.BLUE + bill.getElectricityRecharge().toString() + ConsoleColors.RESET);
        System.out.printf("  燃气费充值: %s 元\n", ConsoleColors.BLUE + bill.getGasRecharge().toString() + ConsoleColors.RESET);
        System.out.printf("  充值总计: %s 元\n",
                ConsoleColors.BLUE_BOLD + bill.getWaterRecharge().add(bill.getElectricityRecharge()).add(bill.getGasRecharge()) + ConsoleColors.RESET);

        System.out.println("\n" + ConsoleColors.YELLOW + "住户退租时状态：" + ConsoleColors.RESET);
        System.out.printf("  水表: %s 元 (%s)\n",
            bill.getWaterReadingOut().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + bill.getWaterReadingOut() + ConsoleColors.RESET :
                ConsoleColors.RED + bill.getWaterReadingOut() + ConsoleColors.RESET,
            bill.getWaterReadingOut().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.printf("  电表: %s 元 (%s)\n",
            bill.getElectricityReadingOut().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + bill.getElectricityReadingOut() + ConsoleColors.RESET :
                ConsoleColors.RED + bill.getElectricityReadingOut() + ConsoleColors.RESET,
            bill.getElectricityReadingOut().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.printf("  燃气表: %s 元 (%s)\n",
            bill.getGasReadingOut().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + bill.getGasReadingOut() + ConsoleColors.RESET :
                ConsoleColors.RED + bill.getGasReadingOut() + ConsoleColors.RESET,
            bill.getGasReadingOut().compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        // 计算理论余额
        BigDecimal waterTheoretical = bill.getWaterReadingIn().add(bill.getWaterRecharge());
        BigDecimal electricityTheoretical = bill.getElectricityReadingIn().add(bill.getElectricityRecharge());
        BigDecimal gasTheoretical = bill.getGasReadingIn().add(bill.getGasRecharge());

        System.out.println("\n" + ConsoleColors.YELLOW + "理论余额（入住时抄表余额 + 住户充值金额）：" + ConsoleColors.RESET);
        System.out.printf("  水费理论余额: %s 元 (%s)\n",
            waterTheoretical.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + waterTheoretical + ConsoleColors.RESET :
                ConsoleColors.RED + waterTheoretical + ConsoleColors.RESET,
            waterTheoretical.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.printf("  电费理论余额: %s 元 (%s)\n",
            electricityTheoretical.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + electricityTheoretical + ConsoleColors.RESET :
                ConsoleColors.RED + electricityTheoretical + ConsoleColors.RESET,
            electricityTheoretical.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.printf("  燃气费理论余额: %s 元 (%s)\n",
            gasTheoretical.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + gasTheoretical + ConsoleColors.RESET :
                ConsoleColors.RED + gasTheoretical + ConsoleColors.RESET,
            gasTheoretical.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.GREEN + "余额" + ConsoleColors.RESET :
                ConsoleColors.RED + "欠费" + ConsoleColors.RESET);

        System.out.println("\n" + ConsoleColors.YELLOW + "住户实际消耗金额明细：" + ConsoleColors.RESET);
        BigDecimal waterConsumed = waterTheoretical.subtract(bill.getWaterReadingOut());
        BigDecimal electricityConsumed = electricityTheoretical.subtract(bill.getElectricityReadingOut());
        BigDecimal gasConsumed = gasTheoretical.subtract(bill.getGasReadingOut());

        System.out.printf("  水实际消耗: %s 元 (%s)\n",
            ConsoleColors.BLUE + waterConsumed.abs() + ConsoleColors.RESET,
            waterConsumed.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.RED + "需要支付" + ConsoleColors.RESET :
                ConsoleColors.GREEN + "有余额" + ConsoleColors.RESET);

        System.out.printf("  电实际消耗: %s 元 (%s)\n",
            ConsoleColors.BLUE + electricityConsumed.abs() + ConsoleColors.RESET,
            electricityConsumed.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.RED + "需要支付" + ConsoleColors.RESET :
                ConsoleColors.GREEN + "有余额" + ConsoleColors.RESET);

        System.out.printf("  燃气实际消耗: %s 元 (%s)\n",
            ConsoleColors.BLUE + gasConsumed.abs() + ConsoleColors.RESET,
            gasConsumed.compareTo(BigDecimal.ZERO) >= 0 ?
                ConsoleColors.RED + "需要支付" + ConsoleColors.RESET :
                ConsoleColors.GREEN + "有余额" + ConsoleColors.RESET);

        System.out.println("\n" + ConsoleColors.YELLOW + "住户实际使用量明细：" + ConsoleColors.RESET);
        System.out.printf("  水使用量: %s 吨/立方米\n", ConsoleColors.CYAN + result.getWaterConsumption().toString() + ConsoleColors.RESET);
        System.out.printf("  电使用量: %s 度\n", ConsoleColors.CYAN + result.getElectricityConsumption().toString() + ConsoleColors.RESET);
        System.out.printf("  燃气使用量: %s 立方米\n", ConsoleColors.CYAN + result.getGasConsumption().toString() + ConsoleColors.RESET);

        System.out.println("\n" + ConsoleColors.YELLOW + "费用明细：" + ConsoleColors.RESET);
        System.out.printf("  水费: %s 元\n", ConsoleColors.RED_BOLD + result.getWaterCost().toString() + ConsoleColors.RESET);
        System.out.printf("  电费: %s 元\n", ConsoleColors.RED_BOLD + result.getElectricityCost().toString() + ConsoleColors.RESET);
        System.out.printf("  燃气费: %s 元\n", ConsoleColors.RED_BOLD + result.getGasCost().toString() + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "------------------------" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.GREEN_BOLD + "总计应付费用: %s 元\n" + ConsoleColors.RESET, result.getTotalCost());
        System.out.printf(ConsoleColors.BLUE + "住户充值金额: %s 元\n" + ConsoleColors.RESET, result.getPrepaidAmount());

        // 显示退款信息
        // 退款金额 = 充值金额 - 实际消费金额
        // 正数表示充值多了，需要退款给用户
        // 负数表示充值少了，用户还需要补缴
        if (result.getRefundAmount().compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf(ConsoleColors.GREEN_BOLD + "房东应退款住户: %s 元\n" + ConsoleColors.RESET, result.getRefundAmount());
        } else if (result.getRefundAmount().compareTo(BigDecimal.ZERO) < 0) {
            System.out.printf(ConsoleColors.RED_BOLD + "住户需补缴房东: %s 元\n" + ConsoleColors.RESET, result.getRefundAmount().abs());
        } else {
            System.out.println(ConsoleColors.YELLOW + "充值金额与实际费用一致，住户或房东均无需退款或补缴" + ConsoleColors.RESET);
        }

        // 获取当前日期作为账单生成日期
        String billGenerationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));

        System.out.printf(ConsoleColors.CYAN + "账单生成时间: " + ConsoleColors.RESET + "%s\n", billGenerationDate);
    }
}
