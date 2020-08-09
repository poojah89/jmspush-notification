package com.aut.jmspushnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class JmsPushNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmsPushNotificationApplication.class, args);
	}

}
