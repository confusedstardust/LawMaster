package com.lm.lawmaster.controller;
import com.google.gson.Gson;
import com.lm.lawmaster.entity.DeeseekRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AIController {

    private final Gson gson = new Gson();


    @PostMapping("tellQuestion")
    public Object tallQuestion(@RequestBody String question) throws IOException, UnirestException {
        Unirest.setTimeouts(0, 0);

//DeeseekRequest: 自己的实体类名称

        List<DeeseekRequest.Message> messages = new ArrayList<>();

// question：说你自己想说的话
        question+="回答限制在400字以内";
        messages.add(DeeseekRequest.Message.builder().role("user").content(question).build());

        DeeseekRequest requestBody = DeeseekRequest.builder()
                .model("deepseek-chat")
                .messages(messages)
                .build();
        HttpResponse<String> response = Unirest.post("https://api.deepseek.com/chat/completions")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+"sk-0b4f6ee35d5343e2a6c8a60a9ede3cba")
                .body(gson.toJson(requestBody))
                .asString();
        return  response.getBody();

    }
}