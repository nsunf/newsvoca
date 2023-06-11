package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.CategoryMinor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMinorRepository extends JpaRepository<CategoryMinor, Long> {
    public CategoryMinor findByNameIgnoreCase(String name);
    public CategoryMinor findByPathname(String name);
}
