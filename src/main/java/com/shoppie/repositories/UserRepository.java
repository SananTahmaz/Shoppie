package com.shoppie.repositories;

import com.shoppie.entities.User;
import com.shoppie.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findAllByStatusAndCreatedAtBefore(UserStatus status, LocalDateTime time);

    Boolean existsByEmailIgnoreCase(String email);
}
