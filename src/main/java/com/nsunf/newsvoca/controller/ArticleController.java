package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.dto.ArticleDetailDto;
import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping({ "", "/" })
    public ResponseEntity<List<ArticleDto>> getArticles(
            @RequestParam(name = "major-cat", required = false) String majorCat,
            @RequestParam(name = "minor-cat", required = false) String minorCat)
    {
        List<ArticleDto> dtoList = new ArrayList<>();

        long tmpTime = System.currentTimeMillis();
        try {
            dtoList = articleService.getArticleDtoList(majorCat, minorCat);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        log.info("크롤링 총 소요 시간 : {}", (System.currentTimeMillis() - tmpTime) * 0.001);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{articleId}/{catName}/{slug}")
    public ResponseEntity<ArticleDetailDto> getArticleDto(
            @PathVariable(required = false) Long articleId,
            @PathVariable(required = false) String catName,
            @PathVariable(required = false) String slug
    ) {

        ArticleDetailDto articleDetailDto = articleService.getArticleDetailDto(articleId);
        // view 개수 올리기 필요
        // session 별로

        return new ResponseEntity<>(articleDetailDto, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "done";
    }

}