package pfe.HumanIQ.HumanIQ.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/greeting")
public class GreetingController {

    @GetMapping("/public")
    public ResponseEntity<String> publicGreeting() {
        return ResponseEntity.ok("Hello! This is a public endpoint.");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> securedGreeting() {
        return ResponseEntity.ok("Hello! This is a secured endpoint - requires authentication.");
    }

    @PostMapping("/message")
    public ResponseEntity<String> postMessage(@RequestBody String message) {
        return ResponseEntity.ok("Received message: " + message);
    }
}
