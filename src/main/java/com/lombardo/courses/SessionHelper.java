package com.lombardo.courses;

import spark.Request;

import java.util.HashMap;
import java.util.Map;

public class SessionHelper {
    private static final String FLASH_MESSAGE_KEY = "flash_message";

    public static Map<String, String> flashMessageHelper(Request req) {
        Map<String, String> model = new HashMap<>();
        model.put("username", req.attribute("username"));
        model.put("flashMessage", captureFlashMessage(req));
        return model;
    }


    public static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY, message);
    }


    public static String getFlashMessage(Request req) {
        if (req.session(false) == null ) {
            return null;
        }
        if (!req.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);
    }

    public static String captureFlashMessage(Request req) {
        String message = getFlashMessage(req);
        if (message != null) {
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }
}
