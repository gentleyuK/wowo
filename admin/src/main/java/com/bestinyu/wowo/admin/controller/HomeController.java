package com.bestinyu.wowo.admin.controller;

import com.bestinyu.wowo.common.ResponseTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController extends BaseController {

    @GetMapping(value = "/health")
    @ResponseBody
    public ResponseTO health() {
        return success("I'm fine!");
    }

    @GetMapping(value = "/say_hello")
    @ResponseBody
    public ResponseTO sayHello(@RequestParam String name) {
        return success("hello " + name + " !");
    }

    @PostMapping(value = "/test_post")
    public ResponseTO testPost(@RequestBody String body) {
        return success(body);
    }

}
