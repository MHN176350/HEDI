package com.group.thr.hedi;

import com.group.thr.hedi.Service.Interface.IAuthenticationService;
import com.group.thr.hedi.Service.Interface.IHealthMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HediMain {

    @Autowired
    private IHealthMetricService healthMetricService;
    @Autowired
    private IAuthenticationService authenticationService;

    public static void main(String[] args) {
        SpringApplication.run(HediMain.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        healthMetricService.initializeDefaultMetrics();
        authenticationService.intializeUserData();
        openSwaggerUI();
    }

    private void openSwaggerUI() {
        String url = "http://localhost:8080/swagger-ui/index.html";
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.contains("win")) { // Windows
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) { // Mac
                rt.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) { // Linux
                rt.exec("xdg-open " + url);
            }
            System.out.println("  Automatically opened Swagger UI in your browser.");
        } catch (Exception e) {
            System.out.println("  Could not auto-open browser. Swagger UI is ready at: " + url);
        }
    }
}