package com.waveraven.moveout.calculator.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    // 构造函数、getter和setter方法
    public Bill() {}

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }

    public BigDecimal getWaterReadingIn() {
        return waterReadingIn;
    }

    public void setWaterReadingIn(BigDecimal waterReadingIn) {
        this.waterReadingIn = waterReadingIn;
    }

    public BigDecimal getElectricityReadingIn() {
        return electricityReadingIn;
    }

    public void setElectricityReadingIn(BigDecimal electricityReadingIn) {
        this.electricityReadingIn = electricityReadingIn;
    }

    public BigDecimal getGasReadingIn() {
        return gasReadingIn;
    }

    public void setGasReadingIn(BigDecimal gasReadingIn) {
        this.gasReadingIn = gasReadingIn;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public BigDecimal getWaterReadingOut() {
        return waterReadingOut;
    }

    public void setWaterReadingOut(BigDecimal waterReadingOut) {
        this.waterReadingOut = waterReadingOut;
    }

    public BigDecimal getElectricityReadingOut() {
        return electricityReadingOut;
    }

    public void setElectricityReadingOut(BigDecimal electricityReadingOut) {
        this.electricityReadingOut = electricityReadingOut;
    }

    public BigDecimal getGasReadingOut() {
        return gasReadingOut;
    }

    public void setGasReadingOut(BigDecimal gasReadingOut) {
        this.gasReadingOut = gasReadingOut;
    }
}
