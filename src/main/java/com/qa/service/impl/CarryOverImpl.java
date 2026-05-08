package com.qa.service.impl;

import cn.hutool.core.date.DateUtil;
import com.qa.model.api.PdhOperationApi;
import com.qa.model.sql.PdhOperationSql;
import com.qa.service.CarryOverService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * @Author LiuZhen
 * @Date 2024/10/14
 * @Description
 */

@SuppressWarnings("all")
@Setter
@Getter
@Slf4j
@Component
@Service
public class CarryOverImpl implements CarryOverService {

    @Resource
    PdhOperationSql pdhOperationSql;

    @Resource
    PdhOperationApi pdhOperationApi;

    @Override
    public void carryOver(String applyNo, String invoiceNo) {
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtil.yesterday());
        pdhOperationSql.updatePdhCreditEndTime(createTime, applyNo);
        if (!Optional.ofNullable(invoiceNo).map(String::isEmpty).orElse(true)) {
            pdhOperationSql.updateFinanceInvoicePdhEndTime(createTime, invoiceNo);
        }
        pdhOperationApi.pdhCarryOver();
    }
}
