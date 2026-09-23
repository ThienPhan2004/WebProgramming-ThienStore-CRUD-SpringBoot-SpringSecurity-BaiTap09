package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // Phương thức POST của /login được Spring Security tự sinh ra (formLogin),
    // không cần viết controller xử lý.
}
