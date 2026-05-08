package com.qa.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.qa.common.Response;
import com.qa.service.MsgIdFilterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author LiuZhen
 * @Date 2023/11/24
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/rocket")
@Api(tags = {"【MQ查询】查询MQ数据"})
public class QueryRocketMqDataController {

    @Resource
    MsgIdFilterService msgIdFilterService;

    @GetMapping(value = "/queryInvoiceNo")
    @ApiOperation(value = "查询MQ发票信息", notes = "查询MQ发票信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "topic", value = "主题", dataType = "String", required = true),
            @ApiImplicitParam(name = "queryInvoiceNo", value = "查询内容", dataType = "String", required = true),
            @ApiImplicitParam(name = "queryContent", value = "查询字段,主要查询: invoiceNo", dataType = "String", required = true)
    })
    public Response<List> queryRocketMqData(String topic, String queryContent, String queryInvoiceNo) {
        List<String> mqMsgIdResult = msgIdFilterService.queryMqMsgId(topic);
        List<String> result = msgIdFilterService.queryInvoiceNoResult(mqMsgIdResult, topic, queryContent, queryInvoiceNo);
        return new Response<>(Collections.singletonList(new JSONObject(ImmutableMap.of("result", result))));
    }
}
