package com.pc.club.BcClub.config;

import com.pc.club.BcClub.service.SignService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2021/7/28
 */
@Configuration
@EnableScheduling
public class ScheduleTask {

    @Resource
    private SignService signService;

//    @Scheduled(cron = "0 55 8 ? * * *")
//    public void sign() {
//        signService.sign();
//    }
}
