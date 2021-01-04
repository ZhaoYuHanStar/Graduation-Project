package com.project.toyrentalsystem.toyrentalsystem.toy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName ToyController
 * @Description
 * @Author 赵语涵
 * @Date 2021-01-04 19:48
 */
@Controller
public class ToyController {
    @GetMapping("/")
    public String hello() {
        System.out.println("00000");
        return "index";
    }
}
