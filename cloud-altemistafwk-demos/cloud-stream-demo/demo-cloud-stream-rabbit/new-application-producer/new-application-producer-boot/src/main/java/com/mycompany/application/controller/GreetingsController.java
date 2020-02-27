package com.mycompany.application.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.application.module.model.Greetings;
import com.mycompany.application.module.stream.GreetingsService;

@RestController
public class GreetingsController {
    private final GreetingsService greetingsService;

    public GreetingsController(GreetingsService greetingsService) {
        this.greetingsService = greetingsService;
    }

    @GetMapping("/greetings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void greetings(@RequestParam("message") String message, @RequestParam("day") String day) {
        Greetings greetings = new Greetings(System.currentTimeMillis(),message, day, "RABBITMQ");
        greetingsService.sendGreeting(greetings);
    }
}
