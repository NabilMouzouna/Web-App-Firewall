package ensaf.gtr2.firewall.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ensaf.gtr2.firewall.model.User;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/allowed-path")
    public String allowedPath() {
        return "This is an allowed path.";
    }

    @GetMapping("/blocked-path")
    public String blockedPath() {
        return "This path should be blocked.";
        // Implement proper logic for blocking here, possibly with a firewall filter
    }

    @PostMapping("/sql-injection")
    public String sqlInjection(@RequestBody User user) {
        // Potentially block or sanitize input here to prevent issues
        return "This username: " + user.getUsername() + " should not be visible because access to this route is denied";
    }
}