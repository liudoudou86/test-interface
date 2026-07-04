package com.qa.service;

/**
 * @Author Tesla.liu
 * @Date 2024/10/14
 * @Description
 */
public interface CarryOverService {

    /**
     * 铺底货结转
     *
     * @param applyNo   铺底货批件号
     * @param invoiceNo 金税发票号
     */
    void carryOver(String applyNo, String invoiceNo);
}
