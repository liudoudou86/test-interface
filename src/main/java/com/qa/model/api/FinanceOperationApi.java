package com.qa.model.api;

import com.qa.utils.YamlUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.hamcrest.Matchers;
import org.springframework.stereotype.Service;

/**
 * @Author Tesla.liu
 * @Date 2024/06/13
 * @Description
 */

@Setter
@Getter
@Slf4j
@Service
public class FinanceOperationApi {

    static String gatewayBaseUrl = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Address.Gateway.base"));

    /**
     * 将金税信息同步给财务网关服务
     *
     * @param wsdlData wsdl数据
     */
    public void syncInvoiceToFinanceGateway(String wsdlData) {
        SerenityRest.given()
                .baseUri(gatewayBaseUrl).basePath("/finance-integration/services/acceptErpZtaxReturnInfo")
                .contentType("text/xml;charset=UTF-8")
                .header("SOAPAction", "")
                .body(wsdlData).log().all()
                .when().post()
                .then().log().all()
                .statusCode(200)
                .body(Matchers.containsString("<RSCODE>S</RSCODE>"))
                .extract().response();
    }
}
