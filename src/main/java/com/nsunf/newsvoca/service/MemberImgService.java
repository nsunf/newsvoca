package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.entity.MemberImg;
import com.nsunf.newsvoca.repository.MemberImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberImgService {

    @Value("${memberImgLocation}")
    private String memberImgLocation;

    private final MemberImgRepository memberImgRepository;
    private final FileService fileService;

    public void saveImg(MemberImg memberImg, MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) return;

        if (StringUtils.hasText(memberImg.getFilename())) {
            fileService.deleteFile(memberImgLocation + "/" + memberImg.getFilename());
        }

        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = null;
        String imgUrl = null;

        imgName = fileService.uploadFile(memberImgLocation, oriImgName, multipartFile.getBytes());
        imgUrl = "/resources/img/" + imgName;

        memberImg.setFilename(imgName);
        memberImg.setUrl(imgUrl);
        memberImg.setOriImgName(oriImgName);
        memberImgRepository.save(memberImg);
    }

    public String getMemberImgUrlByEmail(String email) {
        Optional<MemberImg> memberImg = memberImgRepository.findByMemberEmail(email);
        return memberImg.map(MemberImg::getUrl).orElse(null);
    }

}
