package com.epam.ld.module2.testing;

import java.util.HashMap;
import java.util.Map;

/**
 * Mail server class.
 */
public class MailServer {

    public static final Map<String, String> messages = new HashMap();

    /**
     * Send notification.
     *
     * @param addresses      the addresses
     * @param messageContent the message content
     */
    public void send(String addresses, String messageContent) {
        messages.put(addresses, messageContent);
        System.out.println("Message " + messageContent + " was sent to " + addresses);
    }


}
