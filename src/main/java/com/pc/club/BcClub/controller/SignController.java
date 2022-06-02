package com.pc.club.BcClub.controller;

import com.pc.club.BcClub.service.SignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2021/7/28
 */
@RestController
@RequestMapping("/sign/")
public class SignController {

    @Resource
    private SignService signService;

    @PostMapping("sign")
    public void getSign() {
        signService.sign();
    }
}
