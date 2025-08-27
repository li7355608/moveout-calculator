package com.waveraven;

import com.waveraven.moveout.calculator.BillingService;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args) {
        // 创建费用计算服务
        BillingService billingService = new BillingService();

        // 运行计算流程
        billingService.runCalculation();
    }
}