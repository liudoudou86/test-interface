package com.qa.service;

/**
 * @Author Tesla Liu
 * @Date 2025/07/15
 * @Description
 */
public interface ProcessStreamService {


	/**
	 * 更新合同状态至已盖章和核查通过
	 *
	 * @param contractNo 合同编号
	 */
	void contractUpdateVerificationStatus(String contractNo);

	/**
	 * 更新模板状态至已通过
	 *
	 * @param templateId 模板编号
	 */
	void contractUpdateTemplateStatus(String templateId);
}
