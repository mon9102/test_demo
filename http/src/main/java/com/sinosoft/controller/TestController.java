package com.sinosoft.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiParam(name = "num", value = "测试号:56789", required = true)
    @RequestMapping("/{id}")
    public String test(@PathVariable String id) {
        Map<String, String> param = null;
        switch (id) {
            case "5":
                param = param5();
                break;
            case "6":
                param = param6();

                break;
            case "7":
                param = param7();

                break;
            case "8":
                param = param8();

                break;
            case "9":
                param = param9();

                break;

        }
        return "1111";
    }

    private Map<String, String> param5() {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param6() {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param7() {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param8() {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

    private Map<String, String> param9() {
        Map<String, String> param = new HashMap<String, String>(40);

        return param;
    }

}

