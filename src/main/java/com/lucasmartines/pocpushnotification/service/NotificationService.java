package com.lucasmartines.pocpushnotification.service;

import com.lucasmartines.pocpushnotification.controller.dto.FcmTokenRequest;
import com.lucasmartines.pocpushnotification.controller.dto.NotificationRequest;
import com.lucasmartines.pocpushnotification.model.UserNotification;
import com.lucasmartines.pocpushnotification.repository.FirebaseRepository;
import com.lucasmartines.pocpushnotification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {
    private final FirebaseRepository firebaseRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(FirebaseRepository firebaseRepository, NotificationRepository notificationRepository) {
        this.firebaseRepository = firebaseRepository;
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(NotificationRequest request) {
        firebaseRepository.sendNotificationByCustomersAndTenants(request.title(), request.topic(), request.message(), request.customers(), request.tenants())
                .exceptionally(e -> {
                    throw new RuntimeException(e);
                }).join();
    }

    public void updateFcmToken(FcmTokenRequest request) {
        Optional<UserNotification> oldToken = notificationRepository.findByDocument(request.document());

        if (oldToken.isEmpty()) {
            notificationRepository.save(new UserNotification(request.document(), request.token()));
            firebaseRepository.subscribeToTopic(request.customer(), request.tenant(), "all", request.token());
            return;
        }

        firebaseRepository.unsubscribeFromTopic(request.customer(), request.tenant(), "all", request.token())
                .exceptionally(e -> {
                    throw new RuntimeException(e);
                }).join();

        oldToken.get().setToken(request.token());
        firebaseRepository.subscribeToTopic(request.customer(), request.tenant(), "all", request.token())
                .exceptionally(e -> {
                    throw new RuntimeException(e);
                }).join();
        notificationRepository.save(oldToken.get());
    }
}
