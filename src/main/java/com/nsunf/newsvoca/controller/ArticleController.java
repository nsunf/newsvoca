package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
        articleService.getArticles(majorCat, minorCat);
        log.debug("크롤링 총 소요 시간 : {}", (System.currentTimeMillis() - tmpTime) * 0.001);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "done";
    }

}