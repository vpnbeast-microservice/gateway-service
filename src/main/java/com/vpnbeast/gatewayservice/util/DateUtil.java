package com.vpnbeast.gatewayservice.util;

import java.time.LocalDateTime;

public class DateUtil {

    private DateUtil() {

    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static String getCurrentLocalDateTimeString() {
        return LocalDateTime.now().toString();
    }

}
