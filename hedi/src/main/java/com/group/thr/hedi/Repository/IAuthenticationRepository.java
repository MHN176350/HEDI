package com.group.thr.hedi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.group.thr.hedi.Entity.User;

public interface IAuthenticationRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
