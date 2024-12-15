package ensaf.gtr2.firewall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackRoute {

    @RequestMapping("/**")
    public String fallback() {
        return "Welcome Back ! 🤍";
    }
}