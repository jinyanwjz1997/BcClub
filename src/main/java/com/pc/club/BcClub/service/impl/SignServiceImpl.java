package com.pc.club.BcClub.service.impl;

import com.pc.club.BcClub.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2021/7/28
 */
@Service
@Slf4j
public class SignServiceImpl implements SignService {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 打卡
     */
    @Override
    public void sign() {

        String url = "https://oa.teligen-cloud.com:8280/meip/dcardController/getSignList";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cookie", "JSESSIONID=F03867B59C9D5EB50E09782DBB2600B6; searchQuery=; searchFilter=; pageType=0; aaaaa=weijza5aefd2e-2500-479c-95ff-0994c00add00");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<Object> requestEntity = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        log.info(" requestEntity: " + responseEntity.getBody());
    }
}
