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
 * @Description B2B后台系统订单逆向流程
 */

@Slf4j
@RestController
@RequestMapping("/orderReverseProcess")
@Api(tags = {"【订单中心】退货订单流程"})
public class OrderReverseProcessController {

	@Resource
	private OrderCenterService orderCenterService;

	@SneakyThrows
	@GetMapping(value = "/createRefundBillingAndInvoice")
	@ApiOperation(value = "创建退货订单billing及发票", notes = "创建退货订单billing及发票")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "refundEccId", value = "退货EccId", dataType = "String", required = true),
			@ApiImplicitParam(name = "orderNo", value = "发货订单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "refundOrderNo", value = "退货订单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "refundOrderItem", value = "退货订单行号", dataType = "String", required = true),
			@ApiImplicitParam(name = "quantity", value = "订单行退货数量", dataType = "String", required = true)
	})
	public Response<ResponseResultDTO> createRefundBillingAndInvoice(String refundEccId, String orderNo, String refundOrderNo, String refundOrderItem, String quantity) {
		String dnNumber = "220" + RandomUtil.randomNumbers(7);
		String blnNumber = "330" + RandomUtil.randomNumbers(7);
		String invoiceNo = "330" + RandomUtil.randomNumbers(17);
		String materialNumber = "440" + RandomUtil.randomNumbers(7);
		ResponseResultDTO reqDTO = orderCenterService.createReverseOrderBillingAndInvoice(refundEccId, orderNo, refundOrderNo, refundOrderItem, quantity, dnNumber,
				materialNumber, blnNumber, invoiceNo);
		return new Response<>(reqDTO);
	}

	@SneakyThrows
	@GetMapping(value = "/writeOffRefundBillingAndInvoice")
	@ApiOperation(value = "冲销退货订单billing及发票", notes = "冲销退货订单billing及发票")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "refundEccId", value = "退货EccId", dataType = "String", required = true),
			@ApiImplicitParam(name = "orderNo", value = "发货订单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "refundOrderNo", value = "退货订单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "refundOrderItem", value = "退货订单行号", dataType = "String", required = true),
			@ApiImplicitParam(name = "quantity", value = "订单行退货数量", dataType = "String", required = true),
			@ApiImplicitParam(name = "writeOffDnNumber", value = "退货被冲销的交货单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "writeOffMaterialNumber", value = "退货被冲销的流水单号", dataType = "String", required = true),
			@ApiImplicitParam(name = "writeOffBlnNumber", value = "退货被冲销的billing号", dataType = "String", required = true),
			@ApiImplicitParam(name = "writeOffInvoice", value = "退货被作废的金税发票号", dataType = "String", required = true)
	})
	public Response<ResponseResultDTO> writeOffRefundBillingAndInvoice(String refundEccId, String orderNo, String refundOrderNo, String refundOrderItem,
	                                                                   String quantity, String writeOffDnNumber, String writeOffMaterialNumber,
	                                                                   String writeOffBlnNumber, String writeOffInvoice) {
		String materialNumber = "440" + RandomUtil.randomNumbers(7);
		ResponseResultDTO reqDTO = orderCenterService.writeOffReverseOrderBillingAndInvoice(refundEccId, orderNo, refundOrderNo, refundOrderItem, quantity,
				writeOffDnNumber, materialNumber, writeOffMaterialNumber, writeOffBlnNumber, writeOffInvoice, "1");
		return new Response<>(reqDTO);
	}

}
