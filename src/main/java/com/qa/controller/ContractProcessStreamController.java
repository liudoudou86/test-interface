package com.qa.controller;

import com.qa.common.Response;
import com.qa.service.ProcessStreamService;
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
 * @Date 2025/07/15
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/contract")
@Api(tags = {"【合同中心】业务流处理"})
public class ContractProcessStreamController {

	@Resource
	private ProcessStreamService processStreamService;

	/**
	 * 更新合同状态至已盖章和核查通过
	 *
	 * @param contractNo 合同编号
	 * @return 处理结果
	 */
	@GetMapping(value = "/updateContractStatus")
	@ApiOperation(value = "更新合同状态", notes = "更新合同状态")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "contractNo", value = "合同编号", dataType = "String", required = true)
	})
	public Response<Void> updateContractStatus(String contractNo) {
		processStreamService.contractUpdateVerificationStatus(contractNo);
		return Response.success();
	}

	/**
	 * 更新模板状态至已通过
	 *
	 * @param templateId 模板编号
	 * @return 处理结果
	 */
	@GetMapping(value = "/updateContractTemplateStatus")
	@ApiOperation(value = "更新模板状态", notes = "更新模板状态")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "templateId", value = "模板编号", dataType = "String", required = true)
	})
	public Response<Void> updateContractTemplateStatus(String templateId) {
		processStreamService.contractUpdateTemplateStatus(templateId);
		return Response.success();
	}
}
