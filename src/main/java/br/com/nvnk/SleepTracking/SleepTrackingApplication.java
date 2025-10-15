package br.com.nvnk.SleepTracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SleepTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleepTrackingApplication.class, args);
	}

}
