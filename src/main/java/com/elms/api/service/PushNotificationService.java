package com.elms.api.service;

import com.elms.api.dto.pushnotificationoptions.AdditionalData;
import com.elms.api.dto.pushnotificationoptions.OneSignalPushNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class PushNotificationService {

    @Value("${app.one.signal.rest.api.key}")
    private String oneSignalRestApiKey;
    @Value("${app.one.signal.app.id}")
    private String oneSignalAppId;
    @Value("${app.one.signal.end.point.api}")
    private String oneSignalAppEndPointApi;

    @Autowired
    CommonAsyncService commonAsyncService;

    public void pushSingle(List<String> devices, String title, String message, AdditionalData data) {
        try {
            if(devices.size() > 0) {
                OneSignalPushNotificationDto pushNotificationReq = new OneSignalPushNotificationDto(devices, title, message, data);
                commonAsyncService.postMessageToOneSignal(pushNotificationReq);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
