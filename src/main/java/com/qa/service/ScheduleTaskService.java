package com.qa.service;

/**
 * @Author Tesla Liu
 * @Date 2025/02/05
 * @Description
 */
public interface ScheduleTaskService {

	/**
	 * 订单下发合同中心定时任务
	 */
	void orderSendToContract();

	/**
	 * 取消申请单自动审核通过
	 *
	 * @param orderNo 订单号
	 */
	void cancalOrderAutoApproval(String orderNo);

	/**
	 * 生成购销合同补充协议
	 */
	void createSupplementaryAgreement();

}
