package com.example.finalproject3.member.repository;

import com.example.finalproject3.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    Member findByEmail(String email);
}
