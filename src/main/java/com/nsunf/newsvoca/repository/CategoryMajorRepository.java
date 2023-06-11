package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.CategoryMajor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMajorRepository extends JpaRepository<CategoryMajor, Long> {
    public CategoryMajor findByNameIgnoreCase(String name);
    public CategoryMajor findByPathname(String name);
}
