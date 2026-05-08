package com.qa.service.impl;

import com.qa.model.sql.ContractOperationSql;
import com.qa.service.ProcessStreamService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Tesla Liu
 * @Date 2025/07/15
 * @Description
 */

@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
@Component
@Service
public class ProcessStreamImpl implements ProcessStreamService {

	@Resource
	ContractOperationSql contractOperationSql;

	/**
	 * 更新合同状态至已盖章和核查通过
	 *
	 * @param contractNo 合同编号
	 */
	@SneakyThrows
	@Override
	public void contractUpdateVerificationStatus(String contractNo) {
		contractOperationSql.updateContractVerificationStatus(contractNo);
	}

	/**
	 * 更新模板状态至已通过
	 *
	 * @param templateId 模板编号
	 */
	@SneakyThrows
	@Override
	public void contractUpdateTemplateStatus(String templateId) {
		contractOperationSql.updateContractTemplateStatus(templateId);
	}
}
