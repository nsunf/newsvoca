package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.dto.CategoryMajorDto;
import com.nsunf.newsvoca.dto.CategoryMinorDto;
import com.nsunf.newsvoca.entity.CategoryMajor;
import com.nsunf.newsvoca.entity.CategoryMinor;
import com.nsunf.newsvoca.repository.CategoryMajorRepository;
import com.nsunf.newsvoca.repository.CategoryMinorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryMajorRepository categoryMajorRepository;
    private final CategoryMinorRepository categoryMinorRepository;

    private final ModelMapper modelMapper;

    public void initCategories() {
        if (categoryMajorRepository.findAll().size() != 0 || categoryMinorRepository.findAll().size() != 0) return;

        List<CategoryMajor> catMajList = new ArrayList<>();
        CategoryMajor worldCat = CategoryMajor.builder().name("World").pathname("world").build();
        catMajList.add(worldCat);

        List<CategoryMinor> catMinList = new ArrayList<>();
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("Africa").pathname("africa").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("Americas").pathname("americas").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("Asia").pathname("asia").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("Australia").pathname("australia").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("China").pathname("china").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("Europe").pathname("europe").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("India").pathname("india").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("Middle East").pathname("middleeast").build());
        catMinList.add(CategoryMinor.builder().categoryMajor(worldCat).name("United Kingdom").pathname("uk").build());

        catMajList.forEach(categoryMajorRepository::save);
        catMinList.forEach(categoryMinorRepository::save);
    }

    public List<CategoryMajorDto> getMajorCategories() {
        List<CategoryMajor> majorCategories = categoryMajorRepository.findAll();
        return majorCategories
                .stream()
                .map(cat -> modelMapper.map(cat, CategoryMajorDto.class))
                .collect(Collectors.toList());
    }

    public List<CategoryMinorDto> getMinorCategories() {
        List<CategoryMinor> minorCategories = categoryMinorRepository.findAll();
        return minorCategories
                .stream()
                .map(cat -> modelMapper.map(cat, CategoryMinorDto.class))
                .collect(Collectors.toList());
    }
}
