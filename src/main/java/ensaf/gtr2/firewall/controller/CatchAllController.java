package ensaf.gtr2.firewall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatchAllController {

    @RequestMapping("/**")
    public String fallback() {
        return "This is a fallback route.";
    }
}