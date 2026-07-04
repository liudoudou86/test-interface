package com.qa.controller;


import com.qa.common.Response;
import com.qa.service.DataInitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author Tesla.liu
 * @Date 2023/04/23
 * @Description 信用中心数据初始化
 */

@Slf4j
@RestController
@RequestMapping("/credit")
@Api(tags = {"【信用中心】数据初始化"})
public class CreditDataInitController {

    @Resource
    private DataInitService dataInitService;

    /**
     * 清除信用数据
     *
     * @param sapCode 客户SAP编码
     * @return 处理结果
     */
    @GetMapping(value = "/clearCreditData")
    @ApiOperation(value = "清除信用数据", notes = "清除信用数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "sapCode", value = "客户SAP编码", dataType = "String", required = true)
    })
    public Response<Void> clearCreditData(String sapCode) {
        dataInitService.clearCreditData(sapCode);
        return Response.success();
    }


    /**
     * 清除铺底货数据
     *
     * @param sapCode 客户SAP编码
     * @return 处理结果
     */
    @GetMapping(value = "/clearPdhData")
    @ApiOperation(value = "清除铺底货数据", notes = "清除铺底货数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "sapCode", value = "客户SAP编码", dataType = "String", required = true)
    })
    public Response<Void> clearPdhData(String sapCode) {
        dataInitService.clearPdhData(sapCode);
        return Response.success();
    }

}
