package pfe.HumanIQ.HumanIQ;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting")
public class GrettingController {

    @GetMapping
    public String greeting() {
        return "Hello World";
    }

    @GetMapping("/saygoodbye")

    public String sayGoodbye() {
        return "Goodbye World";
    }
}
