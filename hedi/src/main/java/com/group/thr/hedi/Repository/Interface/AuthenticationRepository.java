package com.group.thr.hedi.Repository.Interface;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.group.thr.hedi.Entity.User;

public interface AuthenticationRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
