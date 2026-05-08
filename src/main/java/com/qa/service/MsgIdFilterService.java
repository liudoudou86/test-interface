package com.qa.service;


import java.util.List;

/**
 * @Author LiuZhen
 * @Date 2023/11/24
 * @Description
 */
public interface MsgIdFilterService {

    /**
     * 查询msgId
     * @param topic topic
     * @return 过滤结果
     */
    List<String> queryMqMsgId(String topic);

    /**
     * MQ查询发票数据
     * @param mqMsgIdResult mqMsgIdResult
     * @param topic topic
     * @param queryContent 数量
     * @param queryInvoiceNo 发票号
     * @return 查询结果
     */
    List<String> queryInvoiceNoResult(List<String> mqMsgIdResult, String topic, String queryContent, String queryInvoiceNo);
}
