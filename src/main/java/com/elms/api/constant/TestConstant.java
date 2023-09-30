package com.elms.api.constant;

import java.util.Objects;

public class TestConstant {

    public static final String NOTIFY_CUSTOMER_QUEUE_NAME = "WS_QUEUE_NOTIFY_ORDER";
    public static final String SESSSION_KEY = "user_sesson";

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_SHOP = 2;
    public static final Integer USER_KIND_CUSTOMER = 3;
    public static final Integer USER_KIND_TABLET = 4;
    public static final Integer USER_KIND_MOBILE = 5;
    public static final Integer USER_KIND_WEBSITE = 6;
    public static final Integer USER_KIND_API_PARTNER = 7;
    public static final Integer USER_KIND_BOARD = 8;
    public static final Integer USER_KIND_PAYMENT = 9;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;


    private TestConstant(){
        throw new IllegalStateException("Utility class");
    }

}
