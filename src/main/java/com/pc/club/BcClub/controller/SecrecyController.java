package com.pc.club.BcClub.controller;

import com.pc.club.BcClub.common.Response;
import com.pc.club.BcClub.service.SecrecyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2022/5/12
 */
@RestController
@RequestMapping("/secrecy/")
public class SecrecyController {

    @Resource
    private SecrecyService secrecyService;

    @GetMapping("add-token")
    public Response<String> addToken(@RequestParam String token, @RequestParam(required = false) Boolean special) {
        if (null == special) {
            special = false;
        }
        secrecyService.insertResource(token, special);
        return Response.success("");
    }
    //
    @GetMapping("getAnswer")
    public Response<String> getAnswer(@RequestParam String examId) {
        return Response.success(secrecyService.getAnswer(examId));
    }
}
