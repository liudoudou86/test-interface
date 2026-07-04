package com.qa.model.api;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.qa.mapper.dto.OaApprovalDTO;
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
 * @Date 2023/04/18
 * @Description 运营平台接口处理
 */

@Setter
@Getter
@Slf4j
@Service
public class CreditOperationApi {

	static String gatewayBaseUrl = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Address.Gateway.base"));
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
	 * 临时信用OA审批
	 *
	 * @param oaApprovalDTO OA审批报文
	 */
	public void temporaryCreditOaApproval(OaApprovalDTO oaApprovalDTO) {

		SerenityRest.given()
				.baseUri(mgmtBaseUrl).basePath("/v1/credit/temp-credit/approval")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(oaApprovalDTO).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

	/**
	 * 大批件临时信用申请OA审批
	 *
	 * @param oaApprovalDTO OA审批报文
	 */
	public void largeTemporaryCreditOaApproval(OaApprovalDTO oaApprovalDTO) {

		SerenityRest.given()
				.baseUri(mgmtBaseUrl).basePath("/v1/workflow/call/credit/complete/bulk_approval")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(oaApprovalDTO).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

	/**
	 * 铺底货新增OA审批
	 *
	 * @param oaApprovalDTO OA审批报文
	 */
	public void pdhNewOaApproval(OaApprovalDTO oaApprovalDTO) {

		SerenityRest.given()
				.baseUri(mgmtBaseUrl).basePath("/v1/workflow/call/credit/complete/pdh")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(oaApprovalDTO).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

	/**
	 * 铺底货调整申请表OA审批
	 *
	 * @param oaApprovalDTO OA审批报文
	 */
	public void pdhAdjustOaApproval(OaApprovalDTO oaApprovalDTO) {

		SerenityRest.given()
				.baseUri(mgmtBaseUrl).basePath("/v1/workflow/call/credit/complete/pdh_review")
				.contentType(ContentType.JSON)
				.headers(ImmutableMap.of("ACCESS-TOKEN", loginSystemGetToken(),
						"signature", "88888888"))
				.body(oaApprovalDTO).log().all()
				.when().post()
				.then().log().all()
				.statusCode(200)
				.body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
				.extract().response();
	}

}
