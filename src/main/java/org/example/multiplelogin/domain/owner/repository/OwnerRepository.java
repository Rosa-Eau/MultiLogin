package org.example.multiplelogin.domain.owner.repository;

import org.example.multiplelogin.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String username);

    boolean existsByEmail(String username);
}

