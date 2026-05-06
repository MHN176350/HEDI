package com.group.thr.hedi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import com.group.thr.hedi.Entity.RefreshToken;
import com.group.thr.hedi.Entity.User;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByUserAndRevoked(User user, boolean revoked);
    Optional<List<RefreshToken>>findByUserId(Long uid);
}
