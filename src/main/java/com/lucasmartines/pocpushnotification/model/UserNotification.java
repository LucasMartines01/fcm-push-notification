package com.lucasmartines.pocpushnotification.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_notification_settings")
@Data
public class UserNotification {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;
        private String document;
        private String token;

        public UserNotification() {
        }

        public UserNotification(String document, String token) {
            this.document = document;
            this.token = token;
        }

}
