package com.aut.jmspushnotification.Service;



import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aut.jmspushnotification.model.Notification;
import com.aut.jmspushnotification.repository.PushNotificationRepository;
import com.aut.jmspushnotification.utils.JSONConverter;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Service
public class PushNotificationService {

	@Autowired
	PushNotificationRepository repository;

	@Value("${app.apiKey}")
	private String apiKey;

	@Value("${app.serverKey}")
	private String serverKey;

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public void processNotification(Notification notification) throws Exception {

		String strategy = "utc";
		String pastTimes = "ignore";
		OkHttpClient client = new OkHttpClient();
		String json = "";
		if ((notification.getBroadcast_time() != null && !notification.getBroadcast_time().isEmpty())) {
			json = "{\n" + " \"target\": {\n" + " \"broadcast\": false,\n" + " \"userIds\": [ \""
					+ notification.getTrader_id() + "\"\n" + " ]\n" + " },\n" + " \"runBackgroundHandler\": true,\n"
					+ " \"scheduled\": true,\n" + " \"schedule\": {\n" + " \"sendAt\": \""
					+ notification.getBroadcast_time() + "\",\n" + " \"strategy\": \"" + strategy + "\",\n"
					+ " \"pastTimes\": \"" + pastTimes + "\"\n" + " },\n" + " \"content\": {\n" + " \"title\": \""
					+ notification.getTitle() + "\",\n" + " \"message\": \"" + notification.getMessage() + "\"\n"
					+ " }\n" + "}";

		} else {

			json = "{\n" + " \"target\": {\n" + " \"broadcast\": false,\n" + " \"installIds\": [ \""
					+ notification.getTrader_id() + "\"\n" + " ]\n" + " },\n" + " \"runBackgroundHandler\": true,\n"
					+ " \"content\": {\n" + " \"title\": \"" + notification.getTitle() + "\",\n" + " \"message\": \""
					+ notification.getMessage() + "\"\n" + " }\n" + "}";

		}

		log.info("json ::" + json);
		RequestBody body = RequestBody.create(JSON, json);
		String credentials = Credentials.basic(apiKey, serverKey);
		Request request = new Request.Builder().url("https://messages.kumulos.com/v2/notifications")
				.header("Authorization", credentials).addHeader("Accept", "application/json")

				.post(body).build();

		try (Response response = client.newCall(request).execute()) {
			log.info("result code : " + response.code());
			String respK = response.body().string();
			log.info("result : " + respK);

			if (response.code() == 202) {
				repository.updateStatus(notification.getId(), 100);
				JSONConverter converter = new JSONConverter();
				Map<String, Object> map = converter.convertResponse(respK);

				Map<String, Object> noti = (Map<String, Object>) map.get("notification");
				Integer KId = (Integer) noti.get("id");
				log.info("KId : " + KId);
			//	repository.addKumulosLog(notification.getId(), new BigDecimal(KId), json, respK);
			} else {
				repository.updateStatus(notification.getId(), -10);
			//	repository.addKumulosLog(notification.getId(), null, json, respK);

			}

		} catch (Exception e) {
			log.error("Error occured : " + e.getMessage());
			e.printStackTrace();
			throw new Exception();
		}

	}
}