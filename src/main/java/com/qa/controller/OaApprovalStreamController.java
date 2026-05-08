package com.qa.controller;

import com.qa.common.Response;
import com.qa.service.ApprovalStreamService;
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
 * @Author LiuZhen
 * @Date 2024/08/13
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/oa")
@Api(tags = {"【OA审批】审批流处理"})
public class OaApprovalStreamController {

	@Resource
	private ApprovalStreamService approvalStreamService;


	/**
	 * 临时信用申请OA审批
	 *
	 * @param applicationNo 临时信用批件号
	 * @return 处理结果
	 */
	@GetMapping(value = "/approvalTemporary")
	@ApiOperation(value = "临时信用申请OA审批", notes = "临时信用申请OA审批")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "applicationNo", value = "临时信用批件号", dataType = "String", required = true)
	})
	public Response<Void> oaApprovalTemporary(String applicationNo) {
		approvalStreamService.temporaryCreditOaApproval(applicationNo);
		return Response.success();
	}


	/**
	 * 铺底货新增OA审批
	 *
	 * @param applicationNo 铺底货批件号
	 * @return 处理结果
	 */
	@GetMapping(value = "/approvalPdh")
	@ApiOperation(value = "铺底货新增OA审批", notes = "铺底货新增OA审批")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "applicationNo", value = "铺底货批件号", dataType = "String", required = true)
	})
	public Response<Void> newOaApprovalPdh(String applicationNo) {
		approvalStreamService.pdhNewOaApproval(applicationNo);
		return Response.success();
	}

	/**
	 * 铺底货调整申请表OA审批
	 *
	 * @param applicationNo 铺底货批件号
	 * @return 处理结果
	 */
	@GetMapping(value = "/approvalPdhReview")
	@ApiOperation(value = "铺底货调整申请表OA审批", notes = "铺底货调整申请表OA审批")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "applicationNo", value = "铺底货批件号", dataType = "String", required = true)
	})
	public Response<Void> adjustOaApprovalPdh(String applicationNo) {
		approvalStreamService.pdhAdjustOaApproval(applicationNo);
		return Response.success();
	}

	/**
	 * 整单折扣申请OA审批
	 *
	 * @param applicationNo 整单折扣批件号
	 * @return 处理结果
	 */
	@GetMapping(value = "/approvalDiscount")
	@ApiOperation(value = "整单折扣申请OA审批", notes = "整单折扣申请OA审批")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "applicationNo", value = "整单折扣批件号", dataType = "String", required = true)
	})
	public Response<Void> oaApprovalDiscount(String applicationNo) {
		approvalStreamService.discountOaApproval(applicationNo);
		return Response.success();
	}

	/**
	 * 合同用印OA审批
	 *
	 * @param contractNo 合同编号
	 * @return 处理结果
	 */
	@GetMapping(value = "/approvalContract")
	@ApiOperation(value = "合同用印OA审批", notes = "合同用印OA审批")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "contractNo", value = "合同编号", dataType = "String", required = true)
	})
	public Response<Void> oaApprovalContract(String contractNo) {
		approvalStreamService.contractOaApproval(contractNo);
		return Response.success();
	}


}
