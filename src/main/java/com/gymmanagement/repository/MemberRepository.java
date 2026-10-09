package com.gymmanagement.repository;

import com.gymmanagement.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmailIgnoreCase(String email);
    java.util.Optional<Member> findByEmailIgnoreCase(String email);
    List<Member> findByStatus(String status);
    
    @Query("SELECT m FROM Member m WHERE LOWER(m.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.email) LIKE LOWER(CONCAT('%', :query, '%')) OR m.phone LIKE CONCAT('%', :query, '%')")
    List<Member> searchMembers(String query);

    long countByStatus(String status);
}
