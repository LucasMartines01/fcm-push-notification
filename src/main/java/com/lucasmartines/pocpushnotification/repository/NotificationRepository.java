package com.lucasmartines.pocpushnotification.repository;

import com.lucasmartines.pocpushnotification.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<UserNotification, String> {
    Optional<UserNotification> findByDocument(String document);
}
