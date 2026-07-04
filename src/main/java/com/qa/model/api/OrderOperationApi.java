package com.qa.model.api;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.qa.mapper.vo.WsdlDataVo;
import com.qa.utils.YamlUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.hamcrest.Matchers;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author Tesla.liu
 * @Date 2024/06/06
 * @Description
 */

@Setter
@Getter
@Slf4j
@Service
public class OrderOperationApi {

	static String gatewayBaseUrl = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Address.Gateway.base"));
	static String orderBaseUrl = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Address.Order.base"));
	static String mgmtBaseUrl = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Address.Mgmt.base"));
	String userName = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Account.Platform.username"));
	String password = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Account.Platform.password"));

	/**
	 * 登录医药商业运营平台-首页登录
	 *
	 * @return 返回uniqueId
	 */
	public static String loginSystemGetUniqueId() {
		JSONObject paramenters = new JSONObject();
		paramenters.put("time", "0.7269461984014485");

		Response response = SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/identity/v3/captcha")
				.contentType(ContentType.JSON)
				.body(paramenters).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.extract().response();
		String uniqueId = Optional.ofNullable(response.jsonPath().getString("data.uniqueId")).orElse("NO-UniqueId");
		log.info("Rebate_uniqueId = " + uniqueId);
		return uniqueId;
	}

	/**
	 * 登录医药商业运营平台-获取token
	 *
	 * @return 返回token
	 */
	public String loginSystemGetToken() {
		String uniqueId = loginSystemGetUniqueId();

		JSONObject paramenters = new JSONObject();
		paramenters.put("userName", userName);
		paramenters.put("password", password);
		paramenters.put("uniqueid", uniqueId);
		paramenters.put("checkCodeUniqueId", uniqueId);
		paramenters.put("code", "");
		paramenters.put("instanceId", 1);
		paramenters.put("loginSource", "7668659579");
		paramenters.put("loginType", 0);
		paramenters.put("tenantId", 1);

		Response response = SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/identity/v2/user/token")
				.contentType(ContentType.JSON.toString())
				.header("Application-Key", "fff40a74a4dc2a11bf0d20bfc2ae8f7a")
				.body(paramenters).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body("resultMsg", Matchers.equalTo("success"))
				.extract().response();
		String getToken = Optional.ofNullable(response.jsonPath().getString("data.token")).orElse("NO-TOKEN");
		log.info("Token = " + getToken);
		return getToken;
	}

	/**
	 * 创建正向订单过账发货
	 *
	 * @param date      日期
	 * @param deliverNo 发货DN
	 * @param batchId   批次号
	 * @param batchNum  发货数量
	 * @param orderNo   订单号
	 * @param orderItem 订单行号
	 */
	public void createPositiveOrderDelivery(String date, String deliverNo, String batchId, String batchNum, String eccid, String orderItemQuantityTotal, String orderNo, String orderItem, String materialNumber) {
		SerenityRest.given()
				.baseUri(orderBaseUrl).basePath("/v1/sap/push/sapDelivery")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.createPositiveDelivery(date, deliverNo, batchId, batchNum, eccid, orderItemQuantityTotal, orderNo, orderItem, "YLF1", "601", materialNumber, "2025")).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<data>true</data>"))
				.extract().response();
	}

	/**
	 * 冲销正向订单过账发货
	 *
	 * @param date      日期
	 * @param deliverNo 发货DN
	 * @param batchId   批次号
	 * @param batchNum  发货数量
	 * @param orderNo   订单号
	 * @param orderItem 订单行号
	 */
	public void writeOffPositiveOrderDelivery(String date, String deliverNo, String batchId, String batchNum, String eccid, String orderItemQuantityTotal, String orderNo, String orderItem, String materialNumber, String writeOffMaterialNumber) {
		SerenityRest.given()
				.baseUri(orderBaseUrl).basePath("/v1/sap/push/sapDelivery")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.writeOffPositiveDelivery(date, deliverNo, batchId, batchNum, eccid, orderItemQuantityTotal, orderNo, orderItem, "ZLR1", "602", materialNumber, "2025", "2025", writeOffMaterialNumber)).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<data>true</data>"))
				.extract().response();
	}

	/**
	 * 创建正向订单Billing
	 *
	 * @param eccId     EccId
	 * @param orderItem 订单行
	 * @param date      日期
	 * @param count     数量
	 * @param amount    金额
	 * @param blnNumber billing号
	 */
	public void createPositiveOrderBln(String eccId, String orderItem, String date, String count, String amount, String blnNumber) {
		SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/medicine-mgmt/services/acceptERPBillingInfo")
				.queryParam("wsdl")
				.contentType(ContentType.XML.toString())
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.createOrerBlnWsdl(eccId, orderItem, "ZF2", date, count, amount, blnNumber)).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<RSCODE>S</RSCODE>"))
				.extract().response();
	}

	/**
	 * 冲销正向订单Billing
	 *
	 * @param eccId             EccId
	 * @param orderItem         订单行
	 * @param date              日期
	 * @param count             数量
	 * @param amount            金额
	 * @param blnNumber         billing号
	 * @param writeOffBlnNumber 冲销的billing号
	 */
	public void writeOffPositiveOrderBln(String eccId, String orderItem, String date, String count, String amount, String writeOffBlnNumber, String blnNumber) {
		SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/medicine-mgmt/services/acceptERPBillingInfo")
				.queryParam("wsdl")
				.contentType(ContentType.XML.toString())
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.writeOffOrderBlnWsdl(eccId, orderItem, "ZS1", date, count, amount, writeOffBlnNumber, blnNumber)).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<RSCODE>S</RSCODE>"))
				.extract().response();
	}

	/**
	 * 创建退货订单过账发货
	 *
	 * @param date      日期
	 * @param deliverNo 发货DN
	 * @param batchId   批次号
	 * @param batchNum  发货数量
	 * @param orderNo   订单号
	 * @param orderItem 订单行号
	 */
	public void createReverseOrderDelivery(String date, String deliverNo, String batchId, String batchNum, String eccid, String orderNo, String refundOrderNo, String orderItem, String materialNumber) {
		SerenityRest.given()
				.baseUri(orderBaseUrl).basePath("/v1/sap/push/sapDelivery")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.createReverseDelivery(date, deliverNo, batchId, batchNum, eccid, orderNo, refundOrderNo, orderItem, "YLF1", "657", materialNumber, "2025")).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<data>true</data>"))
				.extract().response();
	}

	/**
	 * 冲销退货订单过账发货
	 *
	 * @param date      日期
	 * @param deliverNo 发货DN
	 * @param batchId   批次号
	 * @param batchNum  发货数量
	 * @param orderNo   订单号
	 * @param orderItem 订单行号
	 */
	public void writeOffReverseOrderDelivery(String date, String deliverNo, String batchId, String batchNum, String eccid, String orderNo, String refundOrderNo, String orderItem, String materialNumber, String writeOffMaterialNumber) {
		SerenityRest.given()
				.baseUri(orderBaseUrl).basePath("/v1/sap/push/sapDelivery")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.writeOffReverseDelivery(date, deliverNo, batchId, batchNum, eccid, orderNo, refundOrderNo, orderItem, "ZLR1", "658", materialNumber, "2025", "2025", writeOffMaterialNumber)).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<data>true</data>"))
				.extract().response();
	}

	/**
	 * 创建退货订单Billing
	 *
	 * @param eccId     EccId
	 * @param orderItem 订单行
	 * @param date      日期
	 * @param count     数量
	 * @param amount    金额
	 * @param blnNumber billing号
	 */
	public void createReverseOrderBln(String eccId, String orderItem, String date, String count, String amount, String blnNumber) {
		SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/medicine-mgmt/services/acceptERPBillingInfo")
				.queryParam("wsdl")
				.contentType(ContentType.XML.toString())
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.createOrerBlnWsdl(eccId, orderItem, "ZRE", date, count, amount, blnNumber)).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<RSCODE>S</RSCODE>"))
				.extract().response();
	}

	/**
	 * 冲销退货订单Billing
	 *
	 * @param eccId             EccId
	 * @param orderItem         订单行
	 * @param date              日期
	 * @param count             数量
	 * @param amount            金额
	 * @param blnNumber         billing号
	 * @param writeOffBlnNumber 冲销的billing号
	 */
	public void writeOffReverseOrderBln(String eccId, String orderItem, String date, String count, String amount, String writeOffBlnNumber, String blnNumber) {
		SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/medicine-mgmt/services/acceptERPBillingInfo")
				.queryParam("wsdl")
				.contentType(ContentType.XML.toString())
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(WsdlDataVo.writeOffOrderBlnWsdl(eccId, orderItem, "ZS2", date, count, amount, writeOffBlnNumber, blnNumber)).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body(Matchers.containsString("<RSCODE>S</RSCODE>"))
				.extract().response();
	}

	/**
	 * 整单折扣申请OA审批
	 *
	 * @param applicationId 整单折扣申请批件ID
	 * @param status        整单折扣申请审批状态
	 */
	public void discountOaApproval(String applicationId, String status) {

		// 创建请求体的JSON对象
		JSONObject paramenters = new JSONObject();
		paramenters.put("id", applicationId);
		paramenters.put("status", status);

		SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/order/v1/promotion/OrderDiscount/updateOrderDiscountStatus")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.queryParams(paramenters).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

	/**
	 * 下发购销合同至合同中心
	 */
	public void orderIssuedContractJob() {
		SerenityRest.given()
				.baseUri(mgmtBaseUrl).basePath("/v1/purchase/contract/task/execute")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body("").log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

	/**
	 * 取消申请单自动审核
	 */
	public void cancelOrderAutoApprovalJob() {

		SerenityRest.given()
				.baseUri(gatewayBaseUrl).basePath("/api/order/v1/purchase/order/cancelOrderItemUnshippedLastMonth")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("signature", "88888888"))
				.queryParam("")
				.log().all().when()
				.get().then()
				.log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

	/**
	 * 生成购销合同补充协议
	 */
	public void createSupplementaryAgreementJob() {
		SerenityRest.given()
				.baseUri(orderBaseUrl).basePath("/v1/purchase/order/queryOrderItemNotCancelCount")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("signature", "88888888"))
				.log().all().when()
				.get().then()
				.log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

}
