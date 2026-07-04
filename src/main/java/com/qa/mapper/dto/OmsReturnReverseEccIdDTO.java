package com.qa.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author Tesla.liu
 * @Date 2023/04/28
 * @Description
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OmsReturnReverseEccIdDTO {

    /**
     * 原订单号
     */
    private String orderId;

    /**
     * 退货订单号
     */
    private String orderReturnId;

    /**
     * 退货EccId
     */
    private String eccOrderID;

    /**
     * 退货返回信息
     */
    private String eccMessage;

    /**
     * 退货审核
     */
    private String erpStatus;

    /**
     * 退货订单行信息
     */
    private List<String> itemIds;


}
