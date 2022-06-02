package com.pc.club.BcClub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.pc.club.BcClub.mapper")
public class BcClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(BcClubApplication.class, args);
	}

}
