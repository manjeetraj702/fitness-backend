package com.fittrack.analytics_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // 🎯 FIXED: Added missing import

@SpringBootApplication
@EnableScheduling //
public class AnalyticsCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsCoreApplication.class, args);
	}

}