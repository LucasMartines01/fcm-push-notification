package com.lucasmartines.pocpushnotification.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NotificationRequest(@NotNull List<String> customers, @NotNull List<String> tenants, @NotBlank String topic,@NotBlank String message,@NotBlank String title) {
}
