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

    @PostMapping("/test/user")
    public String sqlInjection(@RequestBody User user) {
        return "This username: " + user.getUsername() + " should not be visible because access to this route is denied";
    }
}