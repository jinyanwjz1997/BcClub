package com.pc.club.BcClub.service;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2022/5/12
 */
public interface SecrecyService {

    /**
     * 根据token获取全部的resource
     *
     * @param token token
     * @param special special
     */
    void insertResource(String token,  Boolean special);

    /**
     * 获取答案
     *
     * @param examId examId
     */
    String getAnswer(String examId);
}
