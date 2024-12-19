package com.epam.ld.module2.testing;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class TestLoggerExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        log("Ended: " + context.getRequiredTestClass().getName());
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        log("Started: " + context.getRequiredTestClass().getName());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        log(" Ended Test: " + context.getDisplayName());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        log("Started Test: " + context.getDisplayName());
    }

    private void log(String message) {

        Path filePath = getTestLogPath();
        try {
            try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
                bw.write(LocalDateTime.now() + ": " + message);
                bw.newLine();
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private Path getTestLogPath() {
        if (! (getClass().getClassLoader() instanceof URLClassLoader)) {
            throw new IllegalStateException("The code isn't being executed from a test environment");
        }
        URL url = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).findResource("testLog.txt");
        return Paths.get(URI.create(url.toString()));
    }
}
