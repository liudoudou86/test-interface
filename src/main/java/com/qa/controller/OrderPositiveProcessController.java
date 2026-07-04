package com.qa.controller;

import cn.hutool.core.util.RandomUtil;
import com.qa.common.Response;
import com.qa.mapper.dto.ResponseResultDTO;
import com.qa.service.OrderCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Tesla.liu
 * @Date 2023/09/28
 * @Description 订单正向流程
 */

@Slf4j
@RestController
@RequestMapping("/orderPositiveProcess")
@Api(tags = {"【订单中心】正向订单流程"})
public class OrderPositiveProcessController {

	@Resource
	private OrderCenterService orderCenterService;

	@SneakyThrows
	@GetMapping(value = "/createBillingAndInvoice")
	@ApiOperation(value = "创建正向订单billing及发票", notes = "创建正向订单billing及发票")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "eccId", value = "发货EccId", dataType = "String", required = true),
			@ApiImplicitParam(name = "orderNo", value = "发货订单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "orderItem", value = "发货订单行号", dataType = "String", required = true),
			@ApiImplicitParam(name = "quantity", value = "订单行发货数量", dataType = "String", required = true),
			@ApiImplicitParam(name = "amount", value = "订单行总金额", dataType = "String", required = true)
	})
	public Response<ResponseResultDTO> createBillingAndInvoice(String eccId, String orderNo, String orderItem, String quantity, String amount) {
		String dnNumber = "220" + RandomUtil.randomNumbers(7);
		String blnNumber = "330" + RandomUtil.randomNumbers(7);
		String invoiceNo = "330" + RandomUtil.randomNumbers(17);
		String materialNumber = "440" + RandomUtil.randomNumbers(7);
		ResponseResultDTO reqDTO = orderCenterService.createPositiveOrderBillingAndInvoice(eccId, orderNo, orderItem, quantity, amount,
				dnNumber, materialNumber, blnNumber, invoiceNo);
		return new Response<>(reqDTO);
	}

	@SneakyThrows
	@GetMapping(value = "/writeOffBillingAndInvoice")
	@ApiOperation(value = "冲销正向订单billing及发票", notes = "冲销正向订单billing及发票")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "eccId", value = "发货EccId", dataType = "String", required = true),
			@ApiImplicitParam(name = "orderNo", value = "发货订单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "orderItem", value = "发货订单行号", dataType = "String", required = true),
			@ApiImplicitParam(name = "quantity", value = "订单行发货数量", dataType = "String", required = true),
			@ApiImplicitParam(name = "amount", value = "订单行总金额", dataType = "String", required = true),
			@ApiImplicitParam(name = "dnNumber", value = "发货被冲销的交货单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "writeOffMaterialNumber", value = "发货被冲销的流水编号", dataType = "String", required = true),
			@ApiImplicitParam(name = "writeOffBlnNumber", value = "发货被冲销的billing号", dataType = "String", required = true),
			@ApiImplicitParam(name = "invoiceNo", value = "发货被作废的金税发票号", dataType = "String", required = true)
	})
	public Response<ResponseResultDTO> writeOffBillingAndInvoice(String eccId, String orderNo, String orderItem, String quantity, String amount,
	                                                             String dnNumber, String writeOffMaterialNumber, String writeOffBlnNumber, String invoiceNo) {
		String materialNumber = "440" + RandomUtil.randomNumbers(7);
		ResponseResultDTO reqDTO = orderCenterService.writeOffPositiveOrderBillingAndInvoice(eccId, orderNo, orderItem, quantity, amount,
				dnNumber, materialNumber, writeOffMaterialNumber, writeOffBlnNumber, invoiceNo, "1");
		return new Response<>(reqDTO);
	}

}
