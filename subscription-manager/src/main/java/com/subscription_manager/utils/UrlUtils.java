package com.subscription_manager.utils;

import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UrlUtils {

    public static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return s;
        }
    }

    public static String extractQParam(String googleUrl) {
        Pattern p = Pattern.compile("[?&]q=([^&]+)");
        Matcher m = p.matcher(googleUrl);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
