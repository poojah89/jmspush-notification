package com.aut.jmspushnotification.consumer;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aut.jmspushnotification.Service.PushNotificationService;
import com.aut.jmspushnotification.model.Notification;
import com.aut.jmspushnotification.utils.JSONConverter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PushNotification {
	
	@Autowired
	private PushNotificationService service;
	
	@JmsListener(destination = "${app.queue}")
	public void onMessageReceived(final Message message) {

		log.info("inside ---Push Notification-- listener");
		if (message instanceof TextMessage) {
			String data = null;
			try {
				log.info("JMS listener :" + ((TextMessage) message).getText());
				data = ((TextMessage) message).getText();
				JSONConverter converter = new JSONConverter();
				Notification notification = null;
				notification = converter.convertData(data);
				if (notification.getTrader_id() != null) {
					service.processNotification(notification);
				} else {
					throw new IllegalArgumentException("Message must be of type TextMessage");
				}
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}


