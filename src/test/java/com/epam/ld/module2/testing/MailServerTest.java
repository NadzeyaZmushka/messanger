package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(TestLoggerExtension.class)
public class MailServerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testSendMessageWithMock() throws IOException {

        Template template = new Template("#{value}");
        Client client = new Client();
        client.setAddresses("test@domain.com");
        Map<String, String> values = Collections.singletonMap("value", "Test");

        TemplateEngine templateEngine = mock(TemplateEngine.class);
        when(templateEngine.generateMessage(template, values)).thenReturn("Test");

        MailServer mailServer = mock(MailServer.class);

        Messenger messenger = new Messenger(mailServer, templateEngine);

        messenger.sendMessage(client, template, values);

        verify(mailServer).send(client.getAddresses(), "Test");
    }

    @Test
    public void testSendMessageUsingSpy() {
        TemplateEngine engine = new TemplateEngine();
        MailServer server = new MailServer();
        Messenger realMessenger = new Messenger(server, engine);
        Messenger spyMessenger = Mockito.spy(realMessenger);

        Template template = new Template("#{variable}");
        Map<String, String> values = new HashMap<>();
        values.put("variable", "value");
        Client client = new Client();
        client.setAddresses("test@test.com");

        spyMessenger.sendMessage(client, template, values);

        Mockito.verify(spyMessenger, Mockito.times(1))
                .sendMessage(client, template, values);
        Assert.assertTrue(server.messages.containsKey("test@test.com"));
        Assert.assertEquals("value", server.messages.get("test@test.com"));
    }

}
