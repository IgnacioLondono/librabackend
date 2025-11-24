package com.library.users.repository;

import com.library.users.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByToken(String token);

    List<Session> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt < ?1")
    void deleteExpiredSessions(LocalDateTime now);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.userId = ?1")
    void deleteByUserId(Long userId);
}






