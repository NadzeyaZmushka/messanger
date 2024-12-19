package com.epam.ld.module2.testing.template;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Template engine.
 */
public class TemplateEngine {

    public String generateMessage(Template template, Map<String, String> values) {
        String message = template.getContent();
        Matcher m = Pattern.compile("#\\{(.+?)}").matcher(message);
        while (m.find()) {
            String placeholder = m.group(1);
            if(values.containsKey(placeholder)) {
                message = message.replaceAll("#\\{"+ placeholder +"}", values.get(placeholder));
            } else {
                throw new IllegalArgumentException("Missing value for variable: " + placeholder);
            }
        }
        return message;
    }

}
