package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.service.ArticleService;
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

    private final ArticleService articleService;

    @GetMapping({ "", "/" })
    public ResponseEntity<?> getArticles(
            @RequestParam(name = "major-cat", required = false) String majorCat,
            @RequestParam(name = "minor-cat", required = false) String minorCat)
    {
        articleService.getArticles(majorCat, minorCat);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}