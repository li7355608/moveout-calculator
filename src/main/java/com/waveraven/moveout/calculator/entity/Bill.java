package com.waveraven.moveout.calculator.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Bill {
    private LocalDateTime checkInDate;
    // 入住时水表读数
    private BigDecimal waterReadingIn;
    // 入住时电表读数
    private BigDecimal electricityReadingIn;
    // 入住时燃气表读数
    private BigDecimal gasReadingIn;

    private LocalDateTime checkOutDate;
    // 退租时水表读数
    private BigDecimal waterReadingOut;
    // 退租时电表读数
    private BigDecimal electricityReadingOut;
    // 退租时燃气表读数
    private BigDecimal gasReadingOut;

    // 预付款金额
    private BigDecimal prepaidAmount;
    // 付费模式 (prepaid or postpaid)
    private String paymentMode;

    // 构造函数、getter和setter方法
    public Bill() {}

}
