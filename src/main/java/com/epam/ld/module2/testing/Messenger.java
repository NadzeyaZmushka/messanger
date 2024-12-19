package com.epam.ld.module2.testing;


import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Messenger.
 */
public class Messenger {
    private MailServer mailServer;
    private TemplateEngine templateEngine;

    /**
     * Instantiates a new Messenger.
     *
     * @param mailServer     the mail server
     * @param templateEngine the template engine
     */
    public Messenger(MailServer mailServer,
                     TemplateEngine templateEngine) {
        this.mailServer = mailServer;
        this.templateEngine = templateEngine;
    }

    /**
     * Send message.
     *
     * @param client   the client
     * @param template the template
     */
    public void sendMessage(Client client, Template template, Map<String, String> values) {
        String messageContent =
                templateEngine.generateMessage(template, values);
        mailServer.send(client.getAddresses(), messageContent);
    }

    public static void main(String[] args) throws IOException {
        TemplateEngine engine = new TemplateEngine();
        MailServer server = new MailServer();
        Messenger messenger = new Messenger(server, engine);
        Map<String, String> values = new HashMap<>();
        Client client = new Client();
        client.setAddresses("test@test.com");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (args.length == 0) {
            System.out.println("Enter the email: ");
            String email = reader.readLine();
            client.setAddresses(email);
            System.out.println("Enter the template content: ");
            String content = reader.readLine();
            Template template = new Template(content);
            System.out.println("Enter placeholder and value pairs: ");
            String line = null;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] pair = line.split(" ");
                values.put(pair[0], pair[1]);
            }
            messenger.sendMessage(client, template, values);
        } else if (args.length == 2) {
            String inputFilePath = args[0];
            String outputFilePath = args[1];
            try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
                 PrintWriter writer = new PrintWriter(new File(outputFilePath))) {
                String content = br.readLine();
                Template template = new Template(content);
                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] pair = line.split(" ");
                    values.put(pair[0], pair[1]);
                }
                String messageContent = engine.generateMessage(template, values);
                writer.write("Addresses : " + client.getAddresses() + "\n");
                writer.write("Message : " + messageContent);
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}