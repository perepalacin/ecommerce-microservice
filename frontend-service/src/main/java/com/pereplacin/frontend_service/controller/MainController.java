package com.pereplacin.frontend_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String serveMainHtmlFile () {
        return "forward:/index.html";
    }
}
