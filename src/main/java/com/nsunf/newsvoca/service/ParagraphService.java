package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.dto.ParagraphDto;
import com.nsunf.newsvoca.entity.Paragraph;
import com.nsunf.newsvoca.repository.ParagraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParagraphService {
    private final ParagraphRepository paragraphRepository;

    public void saveParagraphs(List<Paragraph> paragraphList) {
        paragraphRepository.saveAll(paragraphList);
    }

    public List<ParagraphDto> getParagraphContents(Long articleId) {
        return paragraphRepository.getParagraphContentsByArticleId(articleId);
    }

}
