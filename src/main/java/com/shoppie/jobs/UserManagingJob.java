package com.shoppie.jobs;

import com.shoppie.entities.User;
import com.shoppie.enums.UserStatus;
import com.shoppie.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserManagingJob {
    private final UserRepository repository;

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldPendingUsers() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        List<User> users = repository.findAllByStatusAndCreatedAtBefore(UserStatus.PENDING, cutoff);
        repository.deleteAll(users);
    }
}
