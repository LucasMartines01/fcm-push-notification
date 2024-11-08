package com.lucasmartines.pocpushnotification.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record FcmTokenRequest(@NotBlank String customer,@NotBlank String tenant,@NotBlank String token,@NotBlank @CPF String document) {
}
