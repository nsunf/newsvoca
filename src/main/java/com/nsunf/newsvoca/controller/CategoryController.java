package com.nsunf.newsvoca.controller;

import com.nsunf.newsvoca.dto.CategoryMajorDto;
import com.nsunf.newsvoca.dto.CategoryMinorDto;
import com.nsunf.newsvoca.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/major")
    public ResponseEntity<List<CategoryMajorDto>> getMajorCategories() {
        return new ResponseEntity<>(categoryService.getMajorCategories(), HttpStatus.OK);
    }

    @GetMapping("/minor")
    public ResponseEntity<List<CategoryMinorDto>> getMinorCategories() {
        return new ResponseEntity<>(categoryService.getMinorCategories(), HttpStatus.OK);
    }
}
