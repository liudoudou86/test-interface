package com.qa.controller;


import com.qa.common.Response;
import com.qa.service.ScheduleTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Tesla Liu
 * @Date 2025/02/05
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/scheduleTask")
@Api(tags = {"【定时任务】接口调用"})
public class ScheduleTaskController {

	@Resource
	private ScheduleTaskService scheduleTaskService;

	@GetMapping(value = "/issueContract")
	@ApiOperation(value = "下发合同中心", notes = "下发合同中心")
	public Response<Void> issueContract() {
		scheduleTaskService.orderSendToContract();
		return Response.success();
	}

	@GetMapping(value = "/cancalOrderAutoApproval")
	@ApiOperation(value = "取消申请单自动审核", notes = "取消申请单自动审核")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "orderNo", value = "订单号", dataType = "String", required = true)
	})
	public Response<Void> cancalOrderAutoApproval(String orderNo) {
		scheduleTaskService.cancalOrderAutoApproval(orderNo);
		return Response.success();
	}

	@GetMapping(value = "/createSupplementaryAgreement")
	@ApiOperation(value = "生成购销合同补充协议", notes = "生成购销合同补充协议")
	public Response<Void> createSupplementaryAgreement() {
		scheduleTaskService.createSupplementaryAgreement();
		return Response.success();
	}

}
