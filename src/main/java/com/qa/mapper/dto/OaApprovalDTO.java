package com.qa.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author LiuZhen
 * @Date 2024/08/13
 * @Description
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OaApprovalDTO {

    /**
     * 批件号
     */
    private String applicationNo;

    /**
     * 审批状态: 1-通过, 0-否决
     */
    private String approvalResult;

}
