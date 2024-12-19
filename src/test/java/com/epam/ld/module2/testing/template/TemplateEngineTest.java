package com.epam.ld.module2.testing.template;

import com.epam.ld.module2.testing.Client;
import com.epam.ld.module2.testing.TestLoggerExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(TestLoggerExtension.class)
public class TemplateEngineTest {

    private TemplateEngine engine;
    private Client client;
    private Template template;

    @BeforeEach
    void setUp() {
        engine = new TemplateEngine();
        client = new Client();
        template = new Template("Hello #{name}, welcome to #{place}");
    }

    @Test
    public void testTemplateEngineValidInput() {
        client.setAddresses("test@mail.com");
        Map<String, String> values = new HashMap() {{
            put("name", "John");
            put("place", "Earth");
        }};

        String result = engine.generateMessage(template, values);

        assertEquals("Hello John, welcome to Earth", result);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    public void testTemplateEngineValidInput_NotOnWindows() {
        client.setAddresses("test@mail.com");
        Map<String, String> values = new HashMap() {{
            put("name", "John");
            put("place", "Earth");
        }};

        String result = engine.generateMessage(template, values);

        assertEquals("Hello John, welcome to Earth", result);
    }

    @Test
    public void testGenerateMessageExceptionWithException() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            Map<String, String> values = new HashMap<>();
            values.put("value1", "Petr");
            Template template = new Template("#{value2}");
            engine.generateMessage(template, values);
        });

        assertEquals("Missing value for variable: value2", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"#{value1}", "#{value2}", "#{value3}"})
    public void parameterizesTestGenerateMessage(String template) {

        Map<String, String> values = new HashMap<>();
        values.put("value1", "Petr");
        values.put("value2", "Ivan");
        values.put("value3", "Anna");

        String message = engine.generateMessage(new Template(template), values);

        if ("#{value1}".equals(template)) {
            assertEquals(message, "Petr");
        } else if ("#{value2}".equals(template)) {
            assertEquals(message, "Ivan");
        } else if ("#{value3}".equals(template)) {
            assertEquals(message, "Anna");
        }
    }

}
