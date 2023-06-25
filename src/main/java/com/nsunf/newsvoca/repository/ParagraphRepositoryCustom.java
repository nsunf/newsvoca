package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ParagraphDto;

import java.util.List;

public interface ParagraphRepositoryCustom {
    List<ParagraphDto> getParagraphContentsByArticleId(Long id);
}
