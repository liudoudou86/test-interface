package com.qa.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author Tesla.liu
 * @Date 2024/06/19
 * @Description
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResponseResultDTO {


    /**
     * 订单发货数量 or 退货验收数量
     */
    private String orderQuantity;

    /**
     * 交货单号
     */
    private String dnNumber;

    /**
     * 流水编号
     */
    private String materialNumber;

    /**
     * billing单号
     */
    private String blnNumber;

    /**
     * 金税票号
     */
    private String invoiceNo;

    /**
     * 预折折扣
     */
    private String preDiscount;

    /**
     * 整单折扣
     */
    private String discount;

    /**
     * 折让折扣
     */
    private String rebate;

    /**
     * 折扣金额
     */
    private String discountSum;

    /**
     * 实付款
     */
    private String orderPayment;

}
