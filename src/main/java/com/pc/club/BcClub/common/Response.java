package com.pc.club.BcClub.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description 统一返回值
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2022/1/22
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response<T> {

    /**
     * 返回状态码 1是成功 0是失败
     */
    private String resultCode;

    /**
     * 返回状态信息
     */
    private String resultMsg;

    /**
     * 返回数据
     */
    private T data;

    public static <T> Response<T> success(T data) {
        return new Response<>(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG, data);
    }

    public static <T> Response<T> fail(String msg) {
        return new Response<>(Constants.FAILED_CODE, msg, null);
    }

    public static <T> Response<T> fail(T data) {
        return new Response<>(Constants.FAILED_CODE, Constants.FAILED_MSG, data);
    }
}
