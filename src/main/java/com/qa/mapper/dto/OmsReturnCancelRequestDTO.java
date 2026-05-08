package com.qa.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @Author LiuZhen
 * @Date 2024/07/01
 * @Description
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OmsReturnCancelRequestDTO {

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 返回信息
     */
    private String eccMessage;


    /**
     * erp返回状态
     */
    private String erpStatus;

    /**
     * 取消订单行号
     */
    private List<String> orderItemIdList;
}
