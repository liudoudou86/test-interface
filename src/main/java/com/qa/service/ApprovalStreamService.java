package com.qa.service;

/**
 * @Author LiuZhen
 * @Date 2024/08/13
 * @Description
 */
public interface ApprovalStreamService {


	/**
	 * 临时信用申请OA审批
	 *
	 * @param applicationNo 批件号
	 */
	void temporaryCreditOaApproval(String applicationNo);


	/**
	 * 铺底货新增OA审批
	 *
	 * @param applicationNo 批件号
	 */
	void pdhNewOaApproval(String applicationNo);

	/**
	 * 铺底货调整申请表OA审批
	 *
	 * @param applicationNo 批件号
	 */
	void pdhAdjustOaApproval(String applicationNo);

	/**
	 * 整单折扣申请OA审批
	 *
	 * @param applicationNo 批件号
	 */
	void discountOaApproval(String applicationNo);

	/**
	 * 合同用印OA审批
	 *
	 * @param contractNo 合同编号
	 */
	void contractOaApproval(String contractNo);
}
