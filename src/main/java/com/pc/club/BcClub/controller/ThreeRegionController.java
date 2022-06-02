package com.pc.club.BcClub.controller;

import com.pc.club.BcClub.service.ThreeRegionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2022/5/1
 */
@RestController
@RequestMapping("/three/")
public class ThreeRegionController {

    @Resource
    private ThreeRegionService threeRegionService;

    @GetMapping("region")
    public void threeRegion() {
        threeRegionService.insert();
    }
}
