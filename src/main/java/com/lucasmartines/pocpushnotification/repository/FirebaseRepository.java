package com.lucasmartines.pocpushnotification.repository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.lucasmartines.pocpushnotification.model.Customer;
import com.lucasmartines.pocpushnotification.model.FirebaseCredential;
import com.lucasmartines.pocpushnotification.model.Tenant;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public class FirebaseRepository {
    private final CredentialRepository credentialRepository;
    private final Gson gson;

    public FirebaseRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
        gson = new Gson();
    }

    @Async
    public CompletableFuture<Void> sendNotificationByCustomersAndTenants(
            String title,
            String topic,
            String content,
            List<String> requestCustomers,
            List<String> requestTenants
    ) {
        return CompletableFuture.runAsync(() -> credentialRepository.findAll().forEach(customer -> {
            if (requestCustomers.contains(customer.getName()) || requestCustomers.isEmpty()) {
                customer.getTenants().forEach(tenant -> {
                    if (requestTenants.contains(tenant.getName()) || requestTenants.isEmpty()) {
                        FirebaseApp firebaseApp = getFirebaseApp(tenant.getFirebaseCredential(), tenant.getName());
                        Message message = Message.builder()
                                .setNotification(Notification.builder()
                                        .setTitle(title)
                                        .setBody(content)
                                        .build())
                                .setTopic(topic)
                                .build();

                        try {
                            FirebaseMessaging.getInstance(firebaseApp).send(message);
                        } catch (FirebaseMessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }));
    }

    @Async
    public CompletableFuture<Void> subscribeToTopic(String requestCustomer, String requestTenant, String topic, String token) {
        return CompletableFuture.runAsync(() -> {
            Optional<Customer> customer = credentialRepository.findByName(requestCustomer);

            if (customer.isEmpty()) {
                throw new IllegalArgumentException("Customer not found");
            }

            Optional<Tenant> tenant = customer.get().getTenants().stream()
                    .filter(t -> t.getName().equals(requestTenant))
                    .findFirst();

            if (tenant.isEmpty()) {
                throw new IllegalArgumentException("Tenant not found");
            }

            FirebaseApp firebaseApp = getFirebaseApp(tenant.get().getFirebaseCredential(), requestTenant);
            try {
                FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(List.of(token), topic);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Async
    public CompletableFuture<Void> unsubscribeFromTopic(String requestCustomer, String requestTenant, String topic, String token) {
        return CompletableFuture.runAsync(()-> {
            Optional<Customer> customer = credentialRepository.findByName(requestCustomer);

            if (customer.isEmpty()) {
                throw new IllegalArgumentException("Customer not found");
            }

            Optional<Tenant> tenant = customer.get().getTenants().stream()
                    .filter(t -> t.getName().equals(requestTenant))
                    .findFirst();

            if (tenant.isEmpty()) {
                throw new IllegalArgumentException("Tenant not found");
            }

            FirebaseApp firebaseApp = getFirebaseApp(tenant.get().getFirebaseCredential(), requestTenant);
            try {
                FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(List.of(token), topic);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private FirebaseApp getFirebaseApp(FirebaseCredential credential, String customerName) {
        try {
            Optional<FirebaseApp> existingApp = FirebaseApp.getApps().stream()
                    .filter(app -> app.getName().equals(customerName))
                    .findFirst();

            if (existingApp.isPresent()) {
                return existingApp.get();
            }

            return FirebaseApp.initializeApp(FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(gson.toJson(credential).getBytes())))
                    .build(), customerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
