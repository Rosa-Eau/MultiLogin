package org.example.multiplelogin.domain.user.repository;

import org.example.multiplelogin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    Optional<User> findByNickname(String nickname);
}