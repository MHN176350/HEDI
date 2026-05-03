package com.group.thr.hedi.Repository.Interface;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.group.thr.hedi.Entity.RefreshToken;
import com.group.thr.hedi.Entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByUserAndRevoked(User user, boolean revoked);
}
