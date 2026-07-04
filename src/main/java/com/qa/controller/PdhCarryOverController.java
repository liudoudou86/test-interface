package com.qa.controller;

import com.qa.common.Response;
import com.qa.service.CarryOverService;
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
 * @Date 2024/10/14
 * @Description 铺底货到期结转
 */

@Slf4j
@RestController
@RequestMapping("/pdh")
@Api(tags = {"【信用中心】铺底货到期结转"})
public class PdhCarryOverController {

    @Resource
    CarryOverService carryOverService;

    @GetMapping(value = "/carryOver")
    @ApiOperation(value = "到期结转", notes = "到期结转")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "applicationNo", value = "铺底货批件号", dataType = "String", required = true),
            @ApiImplicitParam(name = "invoiceNo", value = "金税发票号", dataType = "String")
    })
    public Response<Void> pdhCarryOver(String applicationNo, String invoiceNo) {
        carryOverService.carryOver(applicationNo, invoiceNo);
        return Response.success();
    }
}
