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
 * @Author LiuZhen
 * @Date 2024/07/02
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/rebate")
@Api(tags = {"【折让中心】数据初始化"})
public class RebateDataInitController {

    @Resource
    private DataInitService dataInitService;

    /**
     * 清除折让数据
     *
     * @param ztCode 客户中台编码
     * @return 处理结果
     */
    @GetMapping(value = "/clearData")
    @ApiOperation(value = "清除折让数据", notes = "清除折让数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ztCode", value = "客户中台编码", dataType = "String", required = true)
    })
    public Response<Void> clearRebateData(String ztCode) {
        dataInitService.clearRebateData(ztCode);
        return Response.success();
    }
}
