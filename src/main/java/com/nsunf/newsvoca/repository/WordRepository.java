package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
