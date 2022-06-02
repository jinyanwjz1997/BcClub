package com.pc.club.BcClub.service.impl;

import com.pc.club.BcClub.service.ThreeRegionService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2022/5/1
 */
@Service
public class ThreeRegionServiceImpl implements ThreeRegionService {
//1A9F5C60-76CD-4AFE-AC28-133B2DD6D8FD
//    吴淞街道 、 友谊路街道 （原宝山镇）、 张庙街道 、 罗店镇 、 大场镇 、 杨行镇 、 月浦镇 、 罗泾镇 、 顾村镇 、 高境镇 、 庙行镇 、 淞南镇 、 宝山城市工业园区
    @Resource
    private RestTemplate restTemplate;

    @Override
    public void insert() {
        
    }
}
