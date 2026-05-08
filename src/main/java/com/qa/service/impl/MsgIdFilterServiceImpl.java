package com.qa.service.impl;


import com.qa.model.api.RocketMqApi;
import com.qa.mapper.vo.RocketMqVo;
import com.qa.service.MsgIdFilterService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author LiuZhen
 * @Date 2023/11/24
 * @Description
 */

@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
@Component
@Service
public class MsgIdFilterServiceImpl implements MsgIdFilterService {

    @Resource
    private RocketMqVo rocketMqVo;

    @Resource
    private RocketMqApi rocketMqApi;

    @Override
    public List<String> queryMqMsgId(String topic) {
        List<String> data = rocketMqVo.filterMqMsgId(rocketMqApi.getTopicMsgId(topic));
        return data;
    }

    @Override
    public List<String> queryInvoiceNoResult(List<String> mqMsgIdResult, String topic, String queryContent, String queryInvoiceNo) {
        List<String> queryResult = mqMsgIdResult.stream()
                .filter(mqMsgId -> {
                    String messageBody = rocketMqApi.getMessageBody(mqMsgId, topic).jsonPath().getString("data.messageView.messageBody");
                    if (!messageBody.equals("[]")) {
                        String invoiceNo = rocketMqVo.filterMessageBody(messageBody, queryContent);
                        return invoiceNo.equals(queryInvoiceNo);
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        return queryResult;
    }

}
