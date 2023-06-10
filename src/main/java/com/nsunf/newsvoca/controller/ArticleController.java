package com.nsunf.newsvoca.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    @GetMapping({ "", "/" })
    public ResponseEntity<?> getArticles(
            @RequestParam(name = "major-cat", required = false) String majorCat,
            @RequestParam(name = "minor-cat", required = false) String minorCat)
    {
        System.out.println(majorCat);
        System.out.println(minorCat);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}