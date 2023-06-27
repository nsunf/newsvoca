package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.MemberImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {
    public Optional<MemberImg> findByMemberEmail(String email);
}
