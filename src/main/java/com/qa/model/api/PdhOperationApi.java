package com.qa.model.api;


import com.qa.utils.YamlUtil;
import io.restassured.http.ContentType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.hamcrest.Matchers;
import org.springframework.stereotype.Service;

/**
 * @Author Tesla.liu
 * @Date 2024/10/14
 * @Description
 */

@Setter
@Getter
@Slf4j
@Service
public class PdhOperationApi {

    static String creditBaseUrl = String.valueOf(YamlUtil.INSTANCE.getValueByKey("Address.Credit.base"));

    public void pdhCarryOver() {

        SerenityRest.given()
                .baseUri(creditBaseUrl).basePath("/v1/credit/pdh/credit/carry-forward")
                .contentType(ContentType.JSON)
                .log().all()
                .when().get()
                .then().log().all()
                .statusCode(200)
                .body("resultCode", Matchers.equalTo("0"), "resultMsg", Matchers.equalTo("success"))
                .extract().response();
    }
}
