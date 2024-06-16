package com.onedatashare.commonutils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.onedatashare.commonutils.config")
public class CommonUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonUtilsApplication.class, args);
	}

}
