package com.sinosoft.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoure on 2018/5/14.
 */
@Api(value = "test", description = "test", tags = "tag1")
@RestController("/test")
public class TestController {
    private Logger log = LoggerFactory.getLogger(getClass());


    @ApiOperation("test")
    @ApiParam(name = "num", value = ":56789", required = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "测试号",paramType="query", dataType = "String",example="5,6,7,8,9",defaultValue ="5"),
            @ApiImplicitParam(name = "gpg", value = "是否使用加密",paramType="query", dataType = "String", example="0,1" ,defaultValue ="0" )
    })
    @RequestMapping("/{id}/{gpg}")
    public String test(@PathVariable String id,@PathVariable String gpg) {
        Map<String, String> param = null;

        switch (id) {
             case "6":
                param = param6(gpg);

                break;
            case "7":
                param = param7(gpg);

                break;
            case "8":
                param = param8(gpg);

                break;
            case "9":
                param = param9(gpg);

                break;
             default:
                 param = param5(gpg);
                 break;

        }
        return "1111";
    }
    /**
     * @Description: 服文5请求
     * @Param: gpg  1为使用gpg加密
     * @Return: java.util.Map<java.lang.String,java.lang.String>
     * @CreateDate: 2019/4/1 11:21
     */
    private Map<String, String> param5( String gpg) {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param6( String gpg) {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param7( String gpg) {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param8( String gpg) {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param9(String gpg) {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

}

