package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParagraphRepository extends JpaRepository<Paragraph, Long>, ParagraphRepositoryCustom {
}
