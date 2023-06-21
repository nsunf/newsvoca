package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        long tmpTime = System.currentTimeMillis();
        articleService.getArticles(majorCat, minorCat).forEach(System.out::println);
        System.out.println("@@@ " + (System.currentTimeMillis() - tmpTime) * 0.001);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "done";
    }

}