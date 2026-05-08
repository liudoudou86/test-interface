package com.qa.service.impl;

import com.qa.model.api.CreditOperationApi;
import com.qa.model.sql.CreditOperationSql;
import com.qa.model.sql.FinanceOperationSql;
import com.qa.model.sql.RebateOperationSql;
import com.qa.service.DataInitService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author LiuZhen
 * @Date 2023/04/14
 * @Description 数据初始化
 */

@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
@Component
@Service
public class DataInitServiceImpl implements DataInitService {

    @Resource
    CreditOperationApi creditOperationApi;

    @Resource
    FinanceOperationSql financeOperationSQL;

    @Resource
    RebateOperationSql rebateOperationSql;

    @Resource
    CreditOperationSql creditOperationSQL;


    /**
     * 清除折让数据
     *
     * @param ztCode 客户中台编码
     */
    @Override
    public void clearRebateData(String ztCode) {
        // 清除折让相关流水
        rebateOperationSql.clearRabateJournal(ztCode);
        // 清除折让总表数据
        rebateOperationSql.clearRabate(ztCode);
    }

    /**
     * 清除信用数据
     *
     * @param sapCode 客户SAP编码
     */
    @Override
    public void clearCreditData(String sapCode) {
        // 清除信用流水数据
        creditOperationSQL.clearCreditJournal(sapCode);
        // 清除年度信用数据
        creditOperationSQL.clearCredit(sapCode);
    }


    /**
     * 清除铺底货数据
     *
     * @param sapCode 客户SAP编码
     */
    @Override
    public void clearPdhData(String sapCode) {
        // 清除信用流水数据
        creditOperationSQL.clearPdhJournal(sapCode);
        // 清除年度信用数据
        creditOperationSQL.clearPdh(sapCode);
    }


    /**
     * 清除财务数据
     *
     * @param ztCode           中台编码
     * @param organizationCode 销售组织编码
     */
    @Override
    public void clearFinanceData(String ztCode) {
        // 清除财务中心相关流水
        financeOperationSQL.clearFinanceJournal(ztCode);
        // 清除财务中心资金账户
        financeOperationSQL.updateFinanceCashAccount(ztCode);
        // 清除财务中心预付款账户
        financeOperationSQL.updateFinanceDeferredRevenueAccount(ztCode);
        // 清除财务中心赊销账户
        financeOperationSQL.updateFinanceAccountReceivableControlAccount(ztCode);
    }

}
