package com.qa.service.impl;

import com.qa.model.api.OrderOperationApi;
import com.qa.model.sql.OrderOperationSql;
import com.qa.service.ScheduleTaskService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Tesla Liu
 * @Date 2025/02/05
 * @Description
 */

@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
@Component
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

	@Resource
	OrderOperationApi orderOperationApi;

	@Resource
	OrderOperationSql orderOperationSql;

	public void orderSendToContract() {
		orderOperationApi.orderIssuedContractJob();
	}

	public void cancalOrderAutoApproval(String orderNo) {
		orderOperationSql.updateOrderCreateTime(orderNo);
		orderOperationSql.updateOrderItemCreateTime(orderNo);
		orderOperationSql.updateOrderContractCreateTime(orderNo);
		orderOperationSql.updateOrderContractContentCreateTime(orderNo);
		orderOperationApi.cancelOrderAutoApprovalJob();
	}

	public void createSupplementaryAgreement() {
		orderOperationApi.createSupplementaryAgreementJob();
	}

}
