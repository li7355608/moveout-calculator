package com.waveraven;

import com.waveraven.moveout.calculator.service.BillingService;

public class Main {
    public static void main(String[] args) {
        // 创建费用计算服务
        BillingService billingService = new BillingService();

        // 运行计算流程
        billingService.runCalculation();
    }
}