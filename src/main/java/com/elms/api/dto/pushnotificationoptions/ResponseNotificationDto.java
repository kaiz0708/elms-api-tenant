package com.elms.api.dto.pushnotificationoptions;

import lombok.Data;

@Data
public class ResponseNotificationDto {

    private Long id;
    private String tableNumber;
    private String cmd;
}
