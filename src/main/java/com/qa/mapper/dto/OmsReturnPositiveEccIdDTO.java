package com.qa.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author LiuZhen
 * @Date 2023/04/23
 * @Description OMS返回正向订单ECCID
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OmsReturnPositiveEccIdDTO {

    /**
     * 信息
     */
    private String eccMessage;

    /**
     * ECCID
     */
    private String eccOrderId;

    /**
     * ERP审批状态
     */
    private String erpStatus;

    /**
     * 订单号
     */
    private String originalOrderId;

}
