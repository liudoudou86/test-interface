package com.qa.controller;


import com.qa.common.Response;
import com.qa.service.DataInitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Tesla.liu
 * @Date 2023/10/08
 * @Description 财务中心数据初始化
 */

@Slf4j
@RestController
@RequestMapping("/finance")
@Api(tags = {"【财务中心】数据初始化"})
public class FinanceDataInitController {

    @Resource
    private DataInitService dataInitService;

    /**
     * @param ztCode 中台编码
     * @return 返回值
     */
    @GetMapping(value = "/clearData")
    @ApiOperation(value = "清除财务数据", notes = "清除财务数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ztCode", value = "中台编码", dataType = "String", required = true)
    })
    public Response<Void> clearFinanceData(String ztCode) {
        dataInitService.clearFinanceData(ztCode);
        return Response.success();
    }
}
