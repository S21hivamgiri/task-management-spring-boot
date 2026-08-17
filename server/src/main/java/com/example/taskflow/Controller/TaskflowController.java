package com.example.taskflow.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TaskflowController {

    @GetMapping("/api/health")
    public Map<String, String> sayIamAlive() {
        return Map.of(
                "status", "ok",
                "service", "auth-service");
    }
}
