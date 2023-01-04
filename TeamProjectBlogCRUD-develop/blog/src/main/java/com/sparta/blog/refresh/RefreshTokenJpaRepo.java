package com.sparta.blog.refresh;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByKeys(Long key);
}
