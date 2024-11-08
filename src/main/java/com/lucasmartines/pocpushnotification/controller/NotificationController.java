package com.lucasmartines.pocpushnotification.controller;
import com.lucasmartines.pocpushnotification.controller.dto.FcmTokenRequest;
import com.lucasmartines.pocpushnotification.controller.dto.NotificationRequest;
import com.lucasmartines.pocpushnotification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/sendToAll")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.ok("Notification sent successfully");
    }

    @PostMapping("/createOrUpdateFcmToken")
    public ResponseEntity<String> updateFcmToken(@RequestBody FcmTokenRequest request) {
        notificationService.updateFcmToken(request);
        return ResponseEntity.ok("Token updated successfully");
    }
}
