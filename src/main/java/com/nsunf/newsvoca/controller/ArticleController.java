package com.nsunf.newsvoca.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @GetMapping("/")
    public Map<String, String> test(@RequestParam(name = "major-cat", required = false) String majorCat, @RequestParam(name = "minor-cat", required = false) String minorCat) {
        Map<String, String> map = new HashMap<>();
        map.put("name", "nsunf");

        return map;
    }
}