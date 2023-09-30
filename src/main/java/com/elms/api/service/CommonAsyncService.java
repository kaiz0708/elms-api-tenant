package com.elms.api.service;

import com.elms.api.dto.pushnotificationoptions.OneSignalPushNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;


@Service
@Slf4j
public class CommonAsyncService {

    @Autowired
    private EmailService emailService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${app.one.signal.rest.api.key}")
    private String oneSignalRestApiKey;
    @Value("${app.one.signal.app.id}")
    private String oneSignalAppId;
    @Value("${app.one.signal.end.point.api}")
    private String oneSignalAppEndPointApi;

    @Autowired
    @Qualifier("threadPoolExecutor")
    @Getter
    private TaskExecutor taskExecutor;

    @Async
    public void sendEmail(String email, String msg, String subject, boolean html){

        Runnable task3 = () -> {
            try {
                emailService.sendEmail(email,msg,subject,html);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        };
        taskExecutor.execute(task3);
    }

    @Async
    public void pushToFirebase(String url, String data, HttpMethod httpMethod){
        System.out.println("firebase url push: "+url);
        Runnable task3 = () -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> entity = new HttpEntity<>(data, headers);
                ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, entity, String.class);
                log.info("callFirebase>>Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
            } catch (Exception ex) {
                log.error("callFirebase>>error: " + ex.getMessage(), ex);
            }
        };
        taskExecutor.execute(task3);
    }

    @Async
    public void deleteFirebasePath(String url){
        System.out.println("firebase url delete: "+url);
        Runnable task3 = () -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
                log.info("callFirebase>>Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
            } catch (Exception ex) {
                log.error("callFirebase>>error: " + ex.getMessage(), ex);
            }
        };
        taskExecutor.execute(task3);
    }

    @Async
    public void postMessageToOneSignal(OneSignalPushNotificationDto oneSignalPushNotification) {
        Runnable task3 = () -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                oneSignalPushNotification.setAppId(oneSignalAppId);
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String requestBody = mapper.writeValueAsString(oneSignalPushNotification);
                log.info("Send notify body: {}", requestBody);
                URL url = new URL(oneSignalAppEndPointApi);
                String urls = url.toString();
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(oneSignalRestApiKey);
                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
                ResponseEntity<String> response = restTemplate.exchange(urls , HttpMethod.POST, entity, String.class);
                log.info("httpResponse: ===>"+ response.getStatusCode());
            } catch (IOException t) {
                log.error(t.getMessage(), t);
            }
        };
        taskExecutor.execute(task3);
    }
}
