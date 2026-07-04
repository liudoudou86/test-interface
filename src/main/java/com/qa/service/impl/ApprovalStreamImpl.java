package com.qa.service.impl;

import com.qa.mapper.dto.OaApprovalDTO;
import com.qa.model.api.CreditOperationApi;
import com.qa.model.api.OrderOperationApi;
import com.qa.model.sql.ContractOperationSql;
import com.qa.model.sql.OrderOperationSql;
import com.qa.service.ApprovalStreamService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Tesla.liu
 * @Date 2024/08/13
 * @Description
 */

@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
@Component
@Service
public class ApprovalStreamImpl implements ApprovalStreamService {

	@Resource
	CreditOperationApi creditOperationApi;

	@Resource
	OrderOperationApi orderOperationApi;

	@Resource
	OrderOperationSql orderOperationSql;

	@Resource
	ContractOperationSql contractOperationSql;

	/**
	 * 临时信用申请OA审批
	 *
	 * @param applicationNo 批件号
	 */
	@Override
	public void temporaryCreditOaApproval(String applicationNo) {
		creditOperationApi.temporaryCreditOaApproval(OaApprovalDTO.builder()
				.applicationNo(applicationNo)
				.approvalResult("1")
				.build());
	}


	/**
	 * 大批件临时信用申请OA审批
	 *
	 * @param largeApplicationNo 大批件临时信用批件号
	 */
	@Override
	public void largeTemporaryCreditOaApproval(String largeApplicationNo) {
		creditOperationApi.largeTemporaryCreditOaApproval(OaApprovalDTO.builder()
				.applicationNo(largeApplicationNo)
				.approvalResult("1")
				.build());
	}


	/**
	 * 铺底货新增OA审批
	 *
	 * @param applicationNo 批件号
	 */
	@Override
	public void pdhNewOaApproval(String applicationNo) {
		creditOperationApi.pdhNewOaApproval(OaApprovalDTO.builder()
				.applicationNo(applicationNo)
				.approvalResult("1")
				.build());
	}

	/**
	 * 铺底货调整申请表OA审批
	 *
	 * @param applicationNo 批件号
	 */
	@Override
	public void pdhAdjustOaApproval(String applicationNo) {
		creditOperationApi.pdhAdjustOaApproval(OaApprovalDTO.builder()
				.applicationNo(applicationNo)
				.approvalResult("1")
				.build());
	}

	/**
	 * 整单折扣申请OA审批
	 *
	 * @param applicationNo 批件号
	 */
	@SneakyThrows
	@Override
	public void discountOaApproval(String applicationNo) {
		String applicationId = orderOperationSql.selectDiscountApplicationFormId(applicationNo);
		orderOperationApi.discountOaApproval(applicationId, "APPROVED");
	}

	/**
	 * 合同用印OA审批
	 *
	 * @param contractNo 合同编号
	 */
	@SneakyThrows
	@Override
	public void contractOaApproval(String contractNo) {
		contractOperationSql.updateContractApprovalStatus(contractNo);
	}

}
