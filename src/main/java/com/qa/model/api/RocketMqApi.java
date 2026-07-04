package com.qa.model.api;


import cn.hutool.core.date.DateUtil;
import com.qa.utils.YamlUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.hamcrest.Matchers;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tesla.liu
 * @Date 2023/11/24
 * @Description
 */

@Setter
@Getter
@Slf4j
@Service
public class RocketMqApi {

    static String rocketMqUrl = String.valueOf(YamlUtil.INSTANCE.getValueByKey("RocketMq.Finance.address"));

    String begin = cn.hutool.core.date.DateUtil.today() + " 00:00:00";
    String end = cn.hutool.core.date.DateUtil.today() + " 23:59:59";
    String pattern = "yyyy-MM-dd HH:mm:ss";
    long beginStamp = DateUtil.parse(begin, pattern).getTime();
    long endStamp = DateUtil.parse(end, pattern).getTime();

    /**
     * 获取主题返回
     *
     * @param topic topic
     * @return 返回接口响应数据
     */
    public Response getTopicMsgId(String topic) {
        // 请求参数
        Map<String, String> paramenters = new HashMap<>();
        paramenters.put("begin", String.valueOf(beginStamp));
        paramenters.put("end", String.valueOf(endStamp));
        paramenters.put("topic", topic);

        return SerenityRest.given()
                .baseUri(rocketMqUrl).basePath("/message/queryMessageByTopic.query")
                .contentType(ContentType.URLENC)
                .urlEncodingEnabled(true)
                .params(paramenters).log().all()
                .when().get()
                .then().log().all()
                .statusCode(200)
                .body("status", Matchers.equalTo(0))
                .extract().response();
    }

    /**
     * 获取消息体
     *
     * @param msgId msgId
     * @param topic topic
     * @return 返回消息体
     */
    public Response getMessageBody(String msgId, String topic) {
        // 请求参数
        Map<String, String> paramenters = new HashMap<>();
        paramenters.put("msgId", msgId);
        paramenters.put("topic", topic);

        return SerenityRest.given()
                .baseUri(rocketMqUrl).basePath("/message/viewMessage.query")
                .contentType(ContentType.URLENC)
                .urlEncodingEnabled(true)
                .params(paramenters).log().all()
                .when().get()
                .then().log().all()
                .statusCode(200)
                .body("status", Matchers.equalTo(0))
                .extract().response();
    }
}
