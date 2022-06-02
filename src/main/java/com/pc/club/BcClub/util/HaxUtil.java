package com.pc.club.BcClub.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2021/7/28
 */
@Slf4j
public class HaxUtil {

    public static void main(String[] args) {
        log.info("pass " + getHexString("123456aB"));
    }

    public static String getHexString(String passWord){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(passWord.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();

            for (byte aByte : bytes) {
                builder.append(Integer.toHexString((0x000000FF & aByte) | 0xFFFFFF00).substring(6));
            }

            return builder.toString();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return passWord;
    }
}
