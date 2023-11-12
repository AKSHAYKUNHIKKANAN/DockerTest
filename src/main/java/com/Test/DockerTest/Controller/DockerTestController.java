package com.Test.DockerTest.Controller;

import com.Test.DockerTest.Model.Card;
import com.Test.DockerTest.Service.DockerTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class DockerTestController {

    @Autowired
    DockerTestService dockerTestService;
    @GetMapping("/login")
    public String login(@RequestParam("userName") String userName,@RequestParam("password") String password) throws IOException {
        return dockerTestService.login(userName,password);
    }

    @GetMapping("/register")
    public String register(@RequestParam("userName") String userName,@RequestParam("password") String password,@RequestParam("email") String email){
       return dockerTestService.register(userName,password,email);
    }
    @PostMapping("/postCards")
    public String postCard(@RequestBody Card card){

        return dockerTestService.postCard(card);
    }

}
