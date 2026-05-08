package com.qa.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qa.mapper.dto.*;
import com.qa.mapper.vo.WsdlDataVo;
import com.qa.model.api.CreditOperationApi;
import com.qa.model.api.FinanceOperationApi;
import com.qa.model.api.OrderOperationApi;
import com.qa.model.sql.OrderOperationSql;
import com.qa.service.OrderCenterService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author LiuZhen
 * @Date 2023/04/23
 * @Description B2B订单逻辑
 */

@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
@Component
@Service
public class OrderCenterServiceImpl implements OrderCenterService {

	@Resource
	OrderOperationApi orderOperationApi;

	@Resource
	FinanceOperationApi financeOperationApi;

	@Resource
	CreditOperationApi creditOperationApi;

	@Resource
	OrderOperationSql orderOperationSql;

	@Resource
	WsdlDataVo wsdlDataVo;


	// 创建正向订单billing和发票
	public ResponseResultDTO createPositiveOrderBillingAndInvoice(String eccId, String orderNo, String orderItem, String quantity, String amount,
	                                                              String dnNumber, String materialNumber, String blnNumber, String invoiceNo) throws SQLException {
		String date = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
		String orderQuantity = new BigDecimal(quantity).setScale(0, RoundingMode.HALF_UP).toString();
		log.info("订单发货数量: {}", orderQuantity);
		String orderTotalAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).toString();
		log.info("订单行总金额: {}", orderTotalAmount);
		// 查询折扣金额合计
		String quantityTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderQuantityTotal(orderNo, orderItem)).orElse("0").toString()).setScale(0, RoundingMode.HALF_UP).toString();
		String orderAmountUnit = NumberUtil.div(orderTotalAmount, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String orderAmount = NumberUtil.round(NumberUtil.mul(orderAmountUnit, orderQuantity), 2).toString();
		log.info("订单行金额: {}", orderAmount);
		String preDiscountTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderPreDiscountTotal(orderNo, orderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		String preDiscountUnit = NumberUtil.div(preDiscountTotal, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String preDiscount = NumberUtil.round(NumberUtil.mul(preDiscountUnit, orderQuantity), 2).toString();
		log.info("预折折扣: {}", preDiscount);
		String orderDiscountTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderDiscountTotal(orderNo, orderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		String orderDiscountUnit = NumberUtil.div(orderDiscountTotal, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String orderDiscount = NumberUtil.round(NumberUtil.mul(orderDiscountUnit, orderQuantity), 2).toString();
		log.info("整单折扣: {}", orderDiscount);
		String rebateTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderRebateTotal(orderNo, orderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		String rebateUnit = NumberUtil.div(rebateTotal, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String rebate = NumberUtil.round(NumberUtil.mul(rebateUnit, orderQuantity), 2).toString();
		log.info("折让折扣: {}", rebate);
		String discountSum = NumberUtil.round(NumberUtil.add(preDiscount, rebate, orderDiscount), 2).negate().toString();
		log.info("折扣合计: {}", discountSum);
		String orderPayment = NumberUtil.round(NumberUtil.add(orderAmount, discountSum), 2).toString();
		log.info("实付款金额: {}", orderPayment);
		orderOperationApi.createPositiveOrderDelivery(date, dnNumber, "202504", orderQuantity, eccId, quantityTotal, orderNo, orderItem, materialNumber);
		// 单独发送金税订单行折扣金额
		if (new BigDecimal(discountSum).compareTo(BigDecimal.ZERO) != 0) {
			orderOperationApi.createPositiveOrderBln(eccId, orderItem, date, orderQuantity, orderPayment, blnNumber);
			financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceRebateOriWsdl(eccId, date, orderQuantity, invoiceNo, orderItem, orderAmount, discountSum, blnNumber, "", "0"));
		} else {
			orderOperationApi.createPositiveOrderBln(eccId, orderItem, date, orderQuantity, orderPayment, blnNumber);
			financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceNormalOriWsdl(eccId, date, orderQuantity, invoiceNo, orderItem, orderAmount, blnNumber, "", "0"));
		}
		ResponseResultDTO result = new ResponseResultDTO();
		result.setOrderQuantity(orderQuantity);
		result.setDnNumber(dnNumber);
		result.setMaterialNumber(materialNumber);
		result.setBlnNumber(blnNumber);
		result.setInvoiceNo(invoiceNo);
		result.setPreDiscount(preDiscount);
		result.setDiscount(orderDiscount);
		result.setRebate(rebate);
		result.setDiscountSum(discountSum);
		result.setOrderPayment(orderPayment);
		log.info(String.valueOf(result));
		return result;
	}

	// 冲销正向订单billing和发票
	public ResponseResultDTO writeOffPositiveOrderBillingAndInvoice(String eccId, String orderNo, String orderItem, String quantity, String amount,
	                                                                String dnNumber, String materialNumber, String writeOffMaterialNumber,
	                                                                String writeOffBlnNumber, String invoiceNo, String cancelMode) throws SQLException {
		String date = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
		String orderQuantity = new BigDecimal(quantity).setScale(0, RoundingMode.HALF_UP).toString();
		log.info("订单发货数量: {}", orderQuantity);
		String orderTotalAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).toString();
		log.info("订单行总金额: {}", orderTotalAmount);
		String blnNumber = "330" + RandomUtil.randomNumbers(7);
		// 查询折扣金额合计
		String quantityTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderQuantityTotal(orderNo, orderItem)).orElse("0").toString()).setScale(0, RoundingMode.HALF_UP).toString();
		String orderAmountUnit = NumberUtil.div(orderTotalAmount, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String orderAmount = NumberUtil.round(NumberUtil.mul(orderAmountUnit, orderQuantity), 2).toString();
		log.info("订单行金额: {}", orderAmount);
		String preDiscountTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderPreDiscountTotal(orderNo, orderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		String preDiscountUnit = NumberUtil.div(preDiscountTotal, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String preDiscount = NumberUtil.round(NumberUtil.mul(preDiscountUnit, orderQuantity), 2).toString();
		log.info("预折折扣: {}", preDiscount);
		String orderDiscountTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderDiscountTotal(orderNo, orderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		String orderDiscountUnit = NumberUtil.div(orderDiscountTotal, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String orderDiscount = NumberUtil.round(NumberUtil.mul(orderDiscountUnit, orderQuantity), 2).toString();
		log.info("整单折扣: {}", orderDiscount);
		String rebateTotal = new BigDecimal(Optional.ofNullable(orderOperationSql.selectPositiveOrderRebateTotal(orderNo, orderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		String rebateUnit = NumberUtil.div(rebateTotal, quantityTotal, 4, RoundingMode.HALF_UP).toString();
		String rebate = NumberUtil.round(NumberUtil.mul(rebateUnit, orderQuantity), 2).toString();
		log.info("折让折扣: {}", rebate);
		String discountSum = NumberUtil.round(NumberUtil.add(preDiscount, rebate, orderDiscount), 2).negate().toString();
		log.info("折扣合计: {}", discountSum);
		String orderPayment = NumberUtil.round(NumberUtil.add(orderAmount, discountSum), 2).toString();
		log.info("实付款金额: {}", orderPayment);
		// 判断作废方式
		if (!cancelMode.equals("0")) {
			// 红冲作废
			if (new BigDecimal(discountSum).compareTo(BigDecimal.ZERO) != 0) {
				orderOperationApi.writeOffPositiveOrderBln(eccId, orderItem, date, orderQuantity, orderPayment, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceRebateOriWsdl(eccId, date, orderQuantity, invoiceNo, orderItem, orderAmount, discountSum, blnNumber, writeOffBlnNumber, "0"));
			} else {
				orderOperationApi.writeOffPositiveOrderBln(eccId, orderItem, date, orderQuantity, orderPayment, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceNormalOriWsdl(eccId, date, orderQuantity, invoiceNo, orderItem, orderAmount, blnNumber, writeOffBlnNumber, "0"));
			}
		} else {
			// 老作废
			if (new BigDecimal(discountSum).compareTo(BigDecimal.ZERO) != 0) {
				orderOperationApi.writeOffPositiveOrderBln(eccId, orderItem, date, orderQuantity, orderPayment, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceRebateOldWsdl(eccId, date, orderQuantity, invoiceNo, orderItem, orderAmount, discountSum, writeOffBlnNumber, "1"));
			} else {
				orderOperationApi.writeOffPositiveOrderBln(eccId, orderItem, date, orderQuantity, orderPayment, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceNormalOldWsdl(eccId, date, orderQuantity, invoiceNo, orderItem, orderAmount, writeOffBlnNumber, "1"));
			}
		}
		orderOperationApi.writeOffPositiveOrderDelivery(date, dnNumber, "202504", orderQuantity, eccId, quantityTotal, orderNo, orderItem, materialNumber, writeOffMaterialNumber);
		ResponseResultDTO result = new ResponseResultDTO();
		result.setOrderQuantity(quantity);
		result.setDnNumber(dnNumber);
		result.setMaterialNumber(writeOffMaterialNumber);
		result.setBlnNumber(blnNumber);
		result.setPreDiscount(preDiscount);
		result.setDiscount(orderDiscount);
		result.setRebate(rebate);
		result.setDiscountSum(discountSum);
		result.setOrderPayment(orderPayment);
		log.info(String.valueOf(result));
		return result;
	}

	// 创建退货订单billing和发票
	@Override
	public ResponseResultDTO createReverseOrderBillingAndInvoice(String refundEccId, String orderNo, String refundOrderNo, String refundOrderItem, String quantity,
	                                                             String dnNumber, String materialNumber, String blnNumber, String invoiceNo) throws SQLException {
		String date = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
		String orderQuantity = new BigDecimal(quantity).setScale(0, RoundingMode.HALF_UP).toString();
		log.info("订单退货数量: {}", orderQuantity);
		String orderTotalAmount = new BigDecimal(Optional.ofNullable(orderOperationSql.selectReverseOrderTotal(refundOrderNo, refundOrderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).negate().toString();
		log.info("实退款总金额: {}", orderTotalAmount);
		// 查询折扣金额合计
		String preDiscountUnitPrice = new BigDecimal(Optional.ofNullable(orderOperationSql.selectReverseOrderPreDiscountUnitPrice(refundOrderNo, refundOrderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		log.info("折后单价: {}", preDiscountUnitPrice);
		String unitPrice = new BigDecimal(Optional.ofNullable(orderOperationSql.selectReverseOrderUnitPrice(refundOrderNo, refundOrderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		log.info("原始单价: {}", unitPrice);
		String orderPrice = NumberUtil.mul(unitPrice, orderQuantity).negate().toString();
		log.info("原始退货价格: {}", orderPrice);
		String discountUnitPrice = NumberUtil.sub(unitPrice, preDiscountUnitPrice).toString();
		log.info("折扣单价: {}", discountUnitPrice);
		String discountSum = NumberUtil.mul(discountUnitPrice, orderQuantity).toString();
		log.info("折扣合计: {}", discountSum);
		String orderAmount = NumberUtil.round(NumberUtil.mul(preDiscountUnitPrice, orderQuantity), 2).negate().toString();
		log.info("实退款金额: {}", orderAmount);
		String orderRefoundQuantity = new BigDecimal(quantity).setScale(0, RoundingMode.HALF_UP).negate().toString();
		orderOperationApi.createReverseOrderDelivery(date, dnNumber, "202504", orderQuantity, refundEccId, orderNo, refundOrderNo, refundOrderItem, materialNumber);
		// 单独发送金税订单行折扣金额
		if (new BigDecimal(discountSum).compareTo(BigDecimal.ZERO) != 0) {
			orderOperationApi.createReverseOrderBln(refundEccId, refundOrderItem, date, orderRefoundQuantity, orderAmount, blnNumber);
			financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceRebateOriWsdl(refundEccId, date, orderRefoundQuantity, invoiceNo, refundOrderItem, orderPrice, discountSum, blnNumber, "", "0"));
		} else {
			orderOperationApi.createReverseOrderBln(refundEccId, refundOrderItem, date, orderRefoundQuantity, orderAmount, blnNumber);
			financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceNormalOriWsdl(refundEccId, date, orderRefoundQuantity, invoiceNo, refundOrderItem, orderPrice, blnNumber, "", "0"));
		}
		ResponseResultDTO result = new ResponseResultDTO();
		result.setOrderQuantity(orderQuantity);
		result.setDnNumber(dnNumber);
		result.setMaterialNumber(materialNumber);
		result.setBlnNumber(blnNumber);
		result.setInvoiceNo(invoiceNo);
		result.setDiscountSum(discountSum);
		result.setOrderPayment(orderAmount);
		log.info(String.valueOf(result));
		return result;
	}

	// 冲销退货订单billing和发票
	@Override
	public ResponseResultDTO writeOffReverseOrderBillingAndInvoice(String refundEccId, String orderNo, String refundOrderNo, String refundOrderItem, String quantity,
	                                                               String writeOffDnNumber, String materialNumber, String writeOffMaterialNumber,
	                                                               String writeOffBlnNumber, String writeOffInvoice, String cancelMode) throws SQLException {
		String date = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
		String orderQuantity = new BigDecimal(quantity).setScale(0, RoundingMode.HALF_UP).toString();
		log.info("订单退货数量: {}", orderQuantity);
		String orderTotalAmount = new BigDecimal(Optional.ofNullable(orderOperationSql.selectReverseOrderTotal(refundOrderNo, refundOrderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).negate().toString();
		log.info("实退款总金额: {}", orderTotalAmount);
		String blnNumber = "330" + RandomUtil.randomNumbers(7);
		// 查询折扣金额合计
		String preDiscountUnitPrice = new BigDecimal(Optional.ofNullable(orderOperationSql.selectReverseOrderPreDiscountUnitPrice(refundOrderNo, refundOrderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		log.info("折后单价: {}", preDiscountUnitPrice);
		String unitPrice = new BigDecimal(Optional.ofNullable(orderOperationSql.selectReverseOrderUnitPrice(refundOrderNo, refundOrderItem)).orElse("0").toString()).setScale(2, RoundingMode.HALF_UP).toString();
		log.info("原始单价: {}", unitPrice);
		String orderPrice = NumberUtil.mul(unitPrice, orderQuantity).negate().toString();
		log.info("原始退货价格: {}", orderPrice);
		String discountUnitPrice = NumberUtil.sub(unitPrice, preDiscountUnitPrice).toString();
		log.info("折扣单价: {}", discountUnitPrice);
		String discountSum = NumberUtil.mul(discountUnitPrice, orderQuantity).toString();
		log.info("折扣合计: {}", discountSum);
		String orderAmount = NumberUtil.round(NumberUtil.mul(preDiscountUnitPrice, orderQuantity), 2).negate().toString();
		log.info("实退款金额: {}", orderAmount);
		String orderRefoundQuantity = new BigDecimal(quantity).setScale(0, RoundingMode.HALF_UP).negate().toString();
		// 判断作废方式
		if (!cancelMode.equals("0")) {
			// 红冲作废
			if (new BigDecimal(discountSum).compareTo(BigDecimal.ZERO) != 0) {
				orderOperationApi.writeOffReverseOrderBln(refundEccId, refundOrderItem, date, orderRefoundQuantity, orderAmount, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceRebateOriWsdl(refundEccId, date, orderRefoundQuantity, writeOffInvoice, refundOrderItem, orderPrice, discountSum, blnNumber, writeOffBlnNumber, "0"));
			} else {
				orderOperationApi.writeOffReverseOrderBln(refundEccId, refundOrderItem, date, orderRefoundQuantity, orderAmount, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceNormalOriWsdl(refundEccId, date, orderRefoundQuantity, writeOffInvoice, refundOrderItem, orderPrice, blnNumber, writeOffBlnNumber, "0"));
			}
		} else {
			// 老作废
			if (new BigDecimal(discountSum).compareTo(BigDecimal.ZERO) != 0) {
				orderOperationApi.writeOffReverseOrderBln(refundEccId, refundOrderItem, date, orderRefoundQuantity, orderAmount, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceRebateOldWsdl(refundEccId, date, orderRefoundQuantity, writeOffInvoice, refundOrderItem, orderPrice, discountSum, writeOffBlnNumber, "1"));
			} else {
				orderOperationApi.writeOffReverseOrderBln(refundEccId, refundOrderItem, date, orderRefoundQuantity, orderAmount, writeOffBlnNumber, blnNumber);
				financeOperationApi.syncInvoiceToFinanceGateway(wsdlDataVo.invoiceNormalOldWsdl(refundEccId, date, orderRefoundQuantity, writeOffInvoice, refundOrderItem, orderPrice, writeOffBlnNumber, "1"));
			}
		}
		orderOperationApi.writeOffReverseOrderDelivery(date, writeOffDnNumber, "202504", orderQuantity, refundEccId, orderNo, refundOrderNo, refundOrderItem, materialNumber, writeOffMaterialNumber);
		ResponseResultDTO result = new ResponseResultDTO();
		result.setOrderQuantity(orderAmount);
		result.setDnNumber(writeOffDnNumber);
		result.setMaterialNumber(writeOffMaterialNumber);
		result.setBlnNumber(blnNumber);
		result.setInvoiceNo(writeOffInvoice);
		result.setDiscountSum(discountSum);
		result.setOrderPayment(orderAmount);
		log.info(String.valueOf(result));
		return result;
	}

}
