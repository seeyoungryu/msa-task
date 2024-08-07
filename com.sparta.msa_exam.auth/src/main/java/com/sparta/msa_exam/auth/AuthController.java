package com.sparta.msa_exam.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth/signIn")
    public String signIn(@RequestParam String user_id) {
        return "User " + user_id + " signed in successfully.";
    }
}
