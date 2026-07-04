package com.qa.service;

import com.qa.mapper.dto.ResponseResultDTO;

import java.sql.SQLException;

/**
 * @Author Tesla.liu
 * @Date 2023/10/20
 * @Description B2B订单接口层
 */
public interface OrderCenterService {

	/**
	 * 创建正向billing单和金税发票
	 *
	 * @param eccId          EccId
	 * @param orderNo        订单号
	 * @param orderItem      订单行号
	 * @param quantity       订单数量
	 * @param amount         订单金额
	 * @param dnNumber       交货单号
	 * @param materialNumber 流水编号
	 * @param blnNumber      billing号
	 * @param invoiceNo      发票号
	 * @return billing号、发票号
	 * @throws SQLException sql查询异常
	 */
	ResponseResultDTO createPositiveOrderBillingAndInvoice(String eccId, String orderNo, String orderItem, String quantity, String amount, String dnNumber,
	                                                       String materialNumber, String blnNumber, String invoiceNo) throws SQLException;

	/**
	 * 创建正向billing单和金税发票的冲销单
	 *
	 * @param eccId                  EccId
	 * @param orderNo                B2B后台系统订单号
	 * @param orderItem              B2B后台系统订单行号
	 * @param quantity               订单数量
	 * @param amount                 订单金额
	 * @param dnNumber               交货单号
	 * @param materialNumber         流水编号
	 * @param writeOffMaterialNumber 被冲销的流水编号
	 * @param writeOffBlnNumber      被冲销的billing号
	 * @param ticket                 被作废的金税发票号
	 * @param cancelMode             作废方式: 0-老作废，1-红冲作废
	 * @return billing冲销号
	 * @throws SQLException sql查询异常
	 */
	ResponseResultDTO writeOffPositiveOrderBillingAndInvoice(String eccId, String orderNo, String orderItem, String quantity, String amount,
	                                                         String dnNumber, String materialNumber, String writeOffMaterialNumber, String writeOffBlnNumber,
	                                                         String ticket, String cancelMode) throws SQLException;

	/**
	 * 创建退货billing单和金税发票
	 *
	 * @param refundEccId     退货订单ECCID
	 * @param orderNo         正向订单号
	 * @param refundOrderNo   退货订单号
	 * @param refundOrderItem 退货订单行号
	 * @param quantity        退货数量号
	 * @param dnNumber        退货交货单号
	 * @param materialNumber  流水编号
	 * @param blnNumber       退货Billing号
	 * @param invoiceNo       退货金税发票号
	 * @return billing号、发票号
	 * @throws SQLException
	 */
	ResponseResultDTO createReverseOrderBillingAndInvoice(String refundEccId, String orderNo, String refundOrderNo, String refundOrderItem, String quantity,
	                                                      String dnNumber, String materialNumber, String blnNumber, String invoiceNo) throws SQLException;

	/**
	 * 创建退货billing单和金税发票的冲销单
	 *
	 * @param refundEccId            退货订单ECCID
	 * @param orderNo                正向订单号
	 * @param refundOrderNo          退货订单号
	 * @param refundOrderItem        退货订单行号
	 * @param quantity               退货数量号
	 * @param writeOffDnNumber       退货被冲销交货单号
	 * @param materialNumber         流水编号
	 * @param writeOffMaterialNumber 被冲销的流水编号
	 * @param writeOffBlnNumber      退货被冲销Billing号
	 * @param writeOffInvoice        退货被作废金税发票号
	 * @param cancelMode             作废方式
	 * @return billing冲销号
	 * @throws SQLException
	 */
	ResponseResultDTO writeOffReverseOrderBillingAndInvoice(String refundEccId, String orderNo, String refundOrderNo, String refundOrderItem, String quantity,
	                                                        String writeOffDnNumber, String materialNumber, String writeOffMaterialNumber,
	                                                        String writeOffBlnNumber, String writeOffInvoice, String cancelMode) throws SQLException;

}
