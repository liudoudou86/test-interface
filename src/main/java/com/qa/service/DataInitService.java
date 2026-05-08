package com.qa.service;

/**
 * @Author LiuZhen
 * @Date 2023/10/20
 * @Description 数据初始化接口层
 */
public interface DataInitService {

    /**
     * 清除折让数据
     *
     * @param ztCode 客户中台编码
     */
    void clearRebateData(String ztCode);

    /**
     * 清除信用数据
     *
     * @param sapCode 客户SAP编码
     */
    void clearCreditData(String sapCode);

    /**
     * 清除铺底货数据
     *
     * @param sapCode 客户SAP编码
     */
    void clearPdhData(String sapCode);


    /**
     * 清除财务数据
     *
     * @param ztCode 客户中台编码
     */
    void clearFinanceData(String ztCode);

}
